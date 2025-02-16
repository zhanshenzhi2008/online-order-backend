package com.orjrs.miniapp.service;

import java.util.Map;

import com.orjrs.miniapp.entity.Order;

/**
 * 支付服务接口
 */
public interface PaymentService {
    /**
     * 创建支付订单
     *
     * @param order 订单信息
     * @param paymentMethod 支付方式
     * @return 支付参数
     */
    Map<String, Object> createPayment(Order order, String paymentMethod);

    /**
     * 处理支付回调
     *
     * @param params 回调参数
     * @return 处理结果
     */
    boolean handlePaymentCallback(Map<String, String> params);

    /**
     * 查询支付结果
     *
     * @param orderId 订单ID
     * @return 支付状态
     */
    String queryPaymentStatus(String orderId);

    /**
     * 关闭支付订单
     *
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean closePayment(String orderId);

    /**
     * 申请退款
     *
     * @param orderId 订单ID
     * @param amount 退款金额
     * @param reason 退款原因
     * @return 退款结果
     */
    boolean refund(String orderId, Double amount, String reason);

    /**
     * 处理退款回调
     *
     * @param params 回调参数
     * @return 处理结果
     */
    boolean handleRefundCallback(Map<String, String> params);
} 