package com.orjrs.miniapp.controller;

import com.orjrs.common.R;
import com.orjrs.miniapp.entity.Order;
import com.orjrs.miniapp.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "订单管理", description = "订单相关接口")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "创建订单")
    @PostMapping("/create")
    public R<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return R.success(createdOrder);
    }

    @Operation(summary = "获取订单列表")
    @GetMapping("/list")
    public R<List<Order>> getOrderList(
            @Parameter(description = "用户ID") @RequestParam String userId,
            @Parameter(description = "订单状态") @RequestParam(required = false) String status) {
        List<Order> orderList = orderService.getOrderList(userId, status);
        return R.success(orderList);
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/detail")
    public R<Order> getOrderDetail(
            @Parameter(description = "订单ID") @RequestParam String id) {
        Order order = orderService.getOrderDetail(id);
        return order != null ? R.success(order) : R.error("订单不存在");
    }

    @Operation(summary = "取消订单")
    @PostMapping("/cancel")
    public R<Order> cancelOrder(
            @Parameter(description = "订单ID") @RequestParam String id) {
        Order order = orderService.cancelOrder(id);
        return order != null ? R.success(order) : R.error("订单不存在或无法取消");
    }

    @Operation(summary = "支付订单")
    @PostMapping("/pay")
    public R<Order> payOrder(
            @Parameter(description = "订单ID") @RequestParam String id,
            @Parameter(description = "支付方式") @RequestParam String paymentMethod) {
        Order order = orderService.payOrder(id, paymentMethod);
        return order != null ? R.success(order) : R.error("订单不存在或无法支付");
    }
} 