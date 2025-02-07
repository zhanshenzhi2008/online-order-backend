package com.orjrs.admin.integration;

import com.orjrs.BaseTest;
import com.orjrs.admin.entity.goods.Goods;
import com.orjrs.admin.entity.order.Order;
import com.orjrs.admin.entity.order.OrderRefund;
import com.orjrs.admin.entity.order.OrderStatistics;
import com.orjrs.admin.service.goods.GoodsService;
import com.orjrs.admin.service.order.OrderRefundService;
import com.orjrs.admin.service.order.OrderService;
import com.orjrs.admin.service.order.OrderStatisticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderFlowIntegrationTest extends BaseTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private GoodsService goodsService;

    @Test
    @Rollback(false)
    void testCompleteOrderFlow() {
        // 1. 获取商品信息
        Goods goods = goodsService.getDetail(1L);
        int originalStock = goods.getStock();

        // 2. 创建订单
        Order order = new Order();
        order.setUserId(1L);
        order.setShopId(1L);
        order.setOrderNo("TEST" + System.currentTimeMillis());
        order.setStatus("pending");
        order.setTotalPrice(goods.getPrice());
        order.setPackingFee(goods.getPackingFee());
        order.setDeliveryFee(new BigDecimal("5.00"));
        order.setActualAmount(goods.getPrice().add(goods.getPackingFee()).add(new BigDecimal("5.00")));
        order.setAddressId(1L);
        
        orderService.save(order);
        assertNotNull(order.getId());

        // 3. 验证商品库存减少
        Goods updatedGoods = goodsService.getDetail(1L);
        assertEquals(originalStock - 1, updatedGoods.getStock());

        // 4. 完成订单
        order.setStatus("completed");
        order.setPaymentMethod("wechat");
        order.setPaymentTime(LocalDateTime.now());
        orderService.updateById(order);

        // 5. 验证订单统计更新
        List<OrderStatistics> statistics = orderStatisticsService.getStatistics(
            LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
            LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
        );
        assertFalse(statistics.isEmpty());
        OrderStatistics todayStats = statistics.get(0);
        assertTrue(todayStats.getCompletedOrders() > 0);

        // 6. 申请退款
        OrderRefund refund = new OrderRefund();
        refund.setOrderId(order.getId());
        refund.setAmount(order.getActualAmount());
        refund.setReason("测试退款流程");
        orderRefundService.createRefund(refund);
        assertNotNull(refund.getId());

        // 7. 处理退款
        orderRefundService.completeRefund(refund.getId());
        order.setStatus("refunded");
        orderService.updateById(order);

        // 8. 验证退款状态
        OrderRefund completedRefund = orderRefundService.getDetail(refund.getId());
        assertEquals("completed", completedRefund.getStatus());
        assertNotNull(completedRefund.getRefundTime());

        // 9. 验证订单统计更新
        statistics = orderStatisticsService.getStatistics(
            LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
            LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
        );
        todayStats = statistics.get(0);
        assertTrue(todayStats.getRefundedOrders() > 0);

        // 10. 验证商品库存恢复
        Goods finalGoods = goodsService.getDetail(1L);
        assertEquals(originalStock, finalGoods.getStock());
    }
} 