package com.orjrs.admin.controller.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orjrs.admin.entity.order.Order;
import com.orjrs.admin.entity.order.OrderStatistics;
import com.orjrs.admin.service.order.OrderService;
import com.orjrs.admin.service.order.OrderStatisticsService;
import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "订单管理")
@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderStatisticsService orderStatisticsService;

    @ApiOperation("分页查询订单列表")
    @GetMapping("/page")
    public R<IPage<Order>> page(
            @ApiParam("页码") @RequestParam(defaultValue = "1") int page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") int size,
            @ApiParam("订单状态") @RequestParam(required = false) String status,
            @ApiParam("用户ID") @RequestParam(required = false) Long userId,
            @ApiParam("开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        
        IPage<Order> pageResult = orderService.page(page, size, status, userId, startTime, endTime);
        return R.success(pageResult);
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/{id}")
    public R<Order> getDetail(@ApiParam("订单ID") @PathVariable Long id) {
        Order order = orderService.getDetail(id);
        return R.success(order);
    }

    @ApiOperation("取消订单")
    @PostMapping("/{id}/cancel")
    public R<Void> cancel(
            @ApiParam("订单ID") @PathVariable Long id,
            @ApiParam("取消原因") @RequestParam String reason) {
        orderService.cancelOrder(id, reason);
        return R.success();
    }

    @ApiOperation("退款订单")
    @PostMapping("/{id}/refund")
    public R<Void> refund(
            @ApiParam("订单ID") @PathVariable Long id,
            @ApiParam("退款原因") @RequestParam String reason) {
        orderService.refundOrder(id, reason);
        return R.success();
    }

    @ApiOperation("获取订单统计")
    @GetMapping("/statistics")
    public R<List<OrderStatistics>> getStatistics(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<OrderStatistics> statistics = orderStatisticsService.getStatistics(startTime, endTime);
        return R.success(statistics);
    }

    @ApiOperation("导出订单")
    @GetMapping("/export")
    public void exportOrders(
            @ApiParam("订单状态") @RequestParam(required = false) String status,
            @ApiParam("用户ID") @RequestParam(required = false) Long userId,
            @ApiParam("开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            HttpServletResponse response) {
        orderService.exportOrders(status, userId, startTime, endTime, response);
    }
} 