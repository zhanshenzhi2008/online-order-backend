package com.orjrs.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.orjrs.common.exception.BusinessException;
import com.orjrs.config.PaymentConfig;
import com.orjrs.entity.Order;
import com.orjrs.service.OrderService;
import com.orjrs.service.PaymentService;
import com.orjrs.utils.AlipayUtils;
import com.orjrs.utils.JsonUtils;
import com.orjrs.utils.WxPayUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderService orderService;
    private final PaymentConfig paymentConfig;
    private final AlipayClient alipayClient;
    private final CloseableHttpClient wxPayClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createPayment(Order order, String paymentMethod) {
        // 验证订单状态
        if (!"pending".equals(order.getStatus())) {
            throw new BusinessException("订单状态不正确");
        }

        Map<String, Object> payParams = new HashMap<>();
        
        // 根据支付方式生成支付参数
        try {
            switch (paymentMethod) {
                case "wxpay":
                    payParams = createWxPayment(order);
                    break;
                case "alipay":
                    payParams = createAlipayPayment(order);
                    break;
                default:
                    throw new BusinessException("不支持的支付方式");
            }
        } catch (Exception e) {
            log.error("创建支付订单失败", e);
            throw new BusinessException("创建支付订单失败");
        }

        return payParams;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentCallback(Map<String, String> params) {
        try {
            // 验证支付回调参数
            String paymentMethod = params.get("payment_method");
            boolean isValid = false;
            
            switch (paymentMethod) {
                case "wxpay":
                    isValid = WxPayUtils.verifySign(params, paymentConfig.getWxPay().getApiKey());
                    break;
                case "alipay":
                    isValid = AlipayUtils.verifySign(params, paymentConfig.getAliPay().getPublicKey());
                    break;
                default:
                    return false;
            }
            
            if (!isValid) {
                return false;
            }

            // 获取订单信息
            String orderId = params.get("out_trade_no");
            String transactionId = params.get("transaction_id");

            // 更新订单状态
            Order order = orderService.payOrder(orderId, paymentMethod);
            if (order != null) {
                log.info("订单支付成功：orderId={}, transactionId={}", orderId, transactionId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("处理支付回调失败", e);
            return false;
        }
    }

    @Override
    public String queryPaymentStatus(String orderId) {
        Order order = orderService.getOrderDetail(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return order.getStatus();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closePayment(String orderId) {
        Order order = orderService.getOrderDetail(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if ("pending".equals(order.getStatus())) {
            order.setStatus("closed");
            order.setStatusText("已关闭");
            order.setUpdateTime(LocalDateTime.now());
            orderService.updateById(order);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refund(String orderId, Double amount, String reason) {
        Order order = orderService.getOrderDetail(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if (!"paid".equals(order.getStatus())) {
            throw new BusinessException("订单状态不支持退款");
        }

        // 根据支付方式调用不同的退款接口
        boolean result = false;
        try {
            switch (order.getPaymentMethod()) {
                case "wxpay":
                    result = wxRefund(order, amount, reason);
                    break;
                case "alipay":
                    result = alipayRefund(order, amount, reason);
                    break;
                default:
                    throw new BusinessException("不支持的支付方式");
            }

            if (result) {
                order.setStatus("refunding");
                order.setStatusText("退款中");
                order.setUpdateTime(LocalDateTime.now());
                orderService.updateById(order);
            }
        } catch (Exception e) {
            log.error("申请退款失败", e);
            throw new BusinessException("申请退款失败");
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleRefundCallback(Map<String, String> params) {
        try {
            // 验证退款回调参数
            String paymentMethod = params.get("payment_method");
            boolean isValid = false;
            
            switch (paymentMethod) {
                case "wxpay":
                    isValid = WxPayUtils.verifySign(params, paymentConfig.getWxPay().getApiKey());
                    break;
                case "alipay":
                    isValid = AlipayUtils.verifySign(params, paymentConfig.getAliPay().getPublicKey());
                    break;
                default:
                    return false;
            }
            
            if (!isValid) {
                return false;
            }

            // 获取订单信息
            String orderId = params.get("out_trade_no");
            String refundStatus = params.get("refund_status");

            // 更新订单状态
            Order order = orderService.getOrderDetail(orderId);
            if (order != null && "refunding".equals(order.getStatus())) {
                order.setStatus("refunded");
                order.setStatusText("已退款");
                order.setUpdateTime(LocalDateTime.now());
                orderService.updateById(order);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("处理退款回调失败", e);
            return false;
        }
    }

    /**
     * 创建微信支付订单
     */
    private Map<String, Object> createWxPayment(Order order) {
        // TODO: 调用微信支付API创建支付订单
        Map<String, Object> payParams = new HashMap<>();
        payParams.put("appId", paymentConfig.getWxPay().getAppId());
        payParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        payParams.put("nonceStr", generateNonceStr());
        payParams.put("package", "prepay_id=wx123456789");
        payParams.put("signType", "RSA");
        payParams.put("paySign", "sign123456789");
        return payParams;
    }

    /**
     * 创建支付宝支付订单
     */
    private Map<String, Object> createAlipayPayment(Order order) throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(paymentConfig.getAliPay().getNotifyUrl());
        request.setReturnUrl(paymentConfig.getAliPay().getReturnUrl());

        Map<String, Object> bizContent = new HashMap<>();
        bizContent.put("out_trade_no", order.getOrderNo());
        bizContent.put("total_amount", order.getPayAmount());
        bizContent.put("subject", "订单支付");
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

        request.setBizContent(JsonUtils.toJson(bizContent));
        String form = alipayClient.pageExecute(request).getBody();

        Map<String, Object> payParams = new HashMap<>();
        payParams.put("form", form);
        return payParams;
    }

    /**
     * 微信退款
     */
    private boolean wxRefund(Order order, Double amount, String reason) {
        // TODO: 调用微信退款API
        return true;
    }

    /**
     * 支付宝退款
     */
    private boolean alipayRefund(Order order, Double amount, String reason) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        
        Map<String, Object> bizContent = new HashMap<>();
        bizContent.put("out_trade_no", order.getOrderNo());
        bizContent.put("refund_amount", amount);
        bizContent.put("refund_reason", reason);
        
        request.setBizContent(JsonUtils.toJson(bizContent));
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        return response.isSuccess();
    }

    /**
     * 生成随机字符串
     */
    private String generateNonceStr() {
        return String.valueOf(System.nanoTime());
    }
} 