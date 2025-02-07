package com.orjrs.admin.service.order;

import com.orjrs.BaseTest;
import com.orjrs.admin.entity.order.Order;
import com.orjrs.admin.entity.order.OrderStatistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatisticsServiceTest extends BaseTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Test
    void testGetStatistics() {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        
        List<OrderStatistics> statistics = orderStatisticsService.getStatistics(startTime, endTime);
        assertNotNull(statistics);
        assertFalse(statistics.isEmpty());
        assertTrue(statistics.size() >= 3);
        
        // 验证数据正确性
        statistics.forEach(stat -> {
            assertNotNull(stat.getDate());
            assertTrue(stat.getTotalOrders() >= 0);
            assertTrue(stat.getTotalAmount().compareTo(BigDecimal.ZERO) >= 0);
            assertTrue(stat.getCompletedOrders() >= 0);
            assertTrue(stat.getCancelledOrders() >= 0);
            assertTrue(stat.getRefundedOrders() >= 0);
        });
    }

    @Test
    void testGenerateDailyStatistics() {
        orderStatisticsService.generateDailyStatistics();
        
        // 验证昨天的统计数据已生成
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<OrderStatistics> statistics = orderStatisticsService.getStatistics(
            LocalDateTime.of(yesterday, LocalTime.MIN),
            LocalDateTime.of(yesterday, LocalTime.MAX)
        );
        
        assertFalse(statistics.isEmpty());
        OrderStatistics stat = statistics.get(0);
        assertEquals(yesterday, stat.getDate());
    }

    @Test
    void testUpdateStatistics() {
        // 创建一个新的已完成订单
        Order order = new Order();
        order.setStatus("completed");
        order.setActualAmount(new BigDecimal("100.00"));
        order.setCreateTime(LocalDateTime.now());
        
        // 更新统计
        orderStatisticsService.updateStatistics(order);
        
        // 验证今天的统计数据
        LocalDate today = LocalDate.now();
        List<OrderStatistics> statistics = orderStatisticsService.getStatistics(
            LocalDateTime.of(today, LocalTime.MIN),
            LocalDateTime.of(today, LocalTime.MAX)
        );
        
        assertFalse(statistics.isEmpty());
        OrderStatistics stat = statistics.get(0);
        assertEquals(today, stat.getDate());
        assertTrue(stat.getCompletedOrders() > 0);
    }

    @Test
    void testUpdateStatisticsWithCancelledOrder() {
        // 创建一个新的已取消订单
        Order order = new Order();
        order.setStatus("cancelled");
        order.setActualAmount(new BigDecimal("50.00"));
        order.setCreateTime(LocalDateTime.now());
        
        // 更新统计
        orderStatisticsService.updateStatistics(order);
        
        // 验证今天的统计数据
        LocalDate today = LocalDate.now();
        List<OrderStatistics> statistics = orderStatisticsService.getStatistics(
            LocalDateTime.of(today, LocalTime.MIN),
            LocalDateTime.of(today, LocalTime.MAX)
        );
        
        assertFalse(statistics.isEmpty());
        OrderStatistics stat = statistics.get(0);
        assertEquals(today, stat.getDate());
        assertTrue(stat.getCancelledOrders() > 0);
    }

    @Test
    void testUpdateStatisticsWithRefundedOrder() {
        // 创建一个新的已退款订单
        Order order = new Order();
        order.setStatus("refunded");
        order.setActualAmount(new BigDecimal("75.00"));
        order.setCreateTime(LocalDateTime.now());
        
        // 更新统计
        orderStatisticsService.updateStatistics(order);
        
        // 验证今天的统计数据
        LocalDate today = LocalDate.now();
        List<OrderStatistics> statistics = orderStatisticsService.getStatistics(
            LocalDateTime.of(today, LocalTime.MIN),
            LocalDateTime.of(today, LocalTime.MAX)
        );
        
        assertFalse(statistics.isEmpty());
        OrderStatistics stat = statistics.get(0);
        assertEquals(today, stat.getDate());
        assertTrue(stat.getRefundedOrders() > 0);
    }
} 