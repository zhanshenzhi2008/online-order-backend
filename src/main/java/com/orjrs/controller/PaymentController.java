package com.orjrs.controller;

import com.orjrs.common.R;
import com.orjrs.entity.Order;
import com.orjrs.service.OrderService;
import com.orjrs.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "支付管理", description = "支付相关接口")
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    @Operation(summary = "创建支付订单")
    @PostMapping("/create")
    public R<Map<String, Object>> createPayment(
            @Parameter(description = "订单ID") @RequestParam String orderId,
            @Parameter(description = "支付方式") @RequestParam String paymentMethod) {
        Order order = orderService.getOrderDetail(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        Map<String, Object> payParams = paymentService.createPayment(order, paymentMethod);
        return R.ok(payParams);
    }

    @Operation(summary = "支付回调")
    @PostMapping("/callback/{method}")
    public String handleCallback(
            @Parameter(description = "支付方式") @PathVariable String method,
            @RequestParam Map<String, String> params) {
        boolean success = paymentService.handlePaymentCallback(params);
        return success ? "success" : "fail";
    }

    @Operation(summary = "查询支付状态")
    @GetMapping("/status")
    public R<String> queryPaymentStatus(
            @Parameter(description = "订单ID") @RequestParam String orderId) {
        String status = paymentService.queryPaymentStatus(orderId);
        return R.ok(status);
    }

    @Operation(summary = "关闭支付")
    @PostMapping("/close")
    public R<Boolean> closePayment(
            @Parameter(description = "订单ID") @RequestParam String orderId) {
        boolean success = paymentService.closePayment(orderId);
        return R.ok(success);
    }

    @Operation(summary = "申请退款")
    @PostMapping("/refund")
    public R<Boolean> refund(
            @Parameter(description = "订单ID") @RequestParam String orderId,
            @Parameter(description = "退款金额") @RequestParam Double amount,
            @Parameter(description = "退款原因") @RequestParam(required = false) String reason) {
        boolean success = paymentService.refund(orderId, amount, reason);
        return R.ok(success);
    }

    @Operation(summary = "退款回调")
    @PostMapping("/refund/callback/{method}")
    public String handleRefundCallback(
            @Parameter(description = "支付方式") @PathVariable String method,
            @RequestParam Map<String, String> params) {
        boolean success = paymentService.handleRefundCallback(params);
        return success ? "success" : "fail";
    }
} 