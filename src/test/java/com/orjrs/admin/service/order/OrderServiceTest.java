package com.orjrs.admin.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orjrs.BaseTest;
import com.orjrs.admin.entity.order.Order;
import com.orjrs.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest extends BaseTest {

    @Autowired
    private OrderService orderService;

    @Test
    void testPage() {
        IPage<Order> page = orderService.page(1, 10, null, null, null, null);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
        assertEquals(5, page.getRecords().size());
    }

    @Test
    void testPageWithStatus() {
        IPage<Order> page = orderService.page(1, 10, "completed", null, null, null);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
        page.getRecords().forEach(order -> 
            assertEquals("completed", order.getStatus())
        );
    }

    @Test
    void testPageWithUserId() {
        IPage<Order> page = orderService.page(1, 10, null, 1L, null, null);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
        page.getRecords().forEach(order -> 
            assertEquals(1L, order.getUserId())
        );
    }

    @Test
    void testGetDetail() {
        Order order = orderService.getDetail(1L);
        assertNotNull(order);
        assertEquals("O202403200001", order.getOrderNo());
        assertEquals(new BigDecimal("30.00"), order.getActualAmount());
    }

    @Test
    void testGetDetailNotFound() {
        assertThrows(BusinessException.class, () -> orderService.getDetail(999L));
    }

    @Test
    void testCancelOrder() {
        // 取消待处理订单
        orderService.cancelOrder(2L, "测试取消订单");
        Order order = orderService.getById(2L);
        assertEquals("cancelled", order.getStatus());
        assertEquals("测试取消订单", order.getRemark());
    }

    @Test
    void testCancelCompletedOrder() {
        // 尝试取消已完成订单，应该抛出异常
        assertThrows(BusinessException.class, () -> 
            orderService.cancelOrder(1L, "测试取消已完成订单")
        );
    }

    @Test
    void testRefundOrder() {
        // 退款已完成订单
        orderService.refundOrder(1L, "测试退款");
        Order order = orderService.getById(1L);
        assertEquals("refunded", order.getStatus());
        assertEquals("测试退款", order.getRemark());
    }

    @Test
    void testRefundPendingOrder() {
        // 尝试退款待处理订单，应该抛出异常
        assertThrows(BusinessException.class, () -> 
            orderService.refundOrder(2L, "测试退款待处理订单")
        );
    }

    @Test
    void testRefundCancelledOrder() {
        // 尝试退款已取消订单，应该抛出异常
        assertThrows(BusinessException.class, () -> 
            orderService.refundOrder(3L, "测试退款已取消订单")
        );
    }

    @Test
    void testExportOrders() {
        // 测试导出功能不抛出异常
        assertDoesNotThrow(() -> 
            orderService.exportOrders(null, null, null, null, null)
        );
    }

    @Test
    void testExportOrdersWithFilters() {
        // 测试带过滤条件的导出功能不抛出异常
        assertDoesNotThrow(() -> 
            orderService.exportOrders(
                "completed",
                1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                null
            )
        );
    }
} 