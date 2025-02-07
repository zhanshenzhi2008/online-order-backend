package com.orjrs.admin.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orjrs.BaseTest;
import com.orjrs.admin.entity.order.OrderRefund;
import com.orjrs.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderRefundServiceTest extends BaseTest {

    @Autowired
    private OrderRefundService orderRefundService;

    @Test
    void testPage() {
        IPage<OrderRefund> page = orderRefundService.page(1, 10, null, null, null);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
        assertEquals(1, page.getRecords().size());
    }

    @Test
    void testPageWithOrderId() {
        IPage<OrderRefund> page = orderRefundService.page(1, 10, 5L, null, null);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
        page.getRecords().forEach(refund -> 
            assertEquals(5L, refund.getOrderId())
        );
    }

    @Test
    void testPageWithStatus() {
        IPage<OrderRefund> page = orderRefundService.page(1, 10, null, "completed", null);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
        page.getRecords().forEach(refund -> 
            assertEquals("completed", refund.getStatus())
        );
    }

    @Test
    void testGetDetail() {
        OrderRefund refund = orderRefundService.getDetail(1L);
        assertNotNull(refund);
        assertEquals(5L, refund.getOrderId());
        assertEquals(new BigDecimal("43.00"), refund.getAmount());
        assertEquals("商品质量问题", refund.getReason());
    }

    @Test
    void testGetDetailNotFound() {
        assertThrows(BusinessException.class, () -> 
            orderRefundService.getDetail(999L)
        );
    }

    @Test
    void testCreateRefund() {
        OrderRefund refund = new OrderRefund();
        refund.setOrderId(4L);
        refund.setAmount(new BigDecimal("52.00"));
        refund.setReason("测试退款");
        refund.setStatus("pending");

        orderRefundService.createRefund(refund);
        assertNotNull(refund.getId());

        OrderRefund saved = orderRefundService.getById(refund.getId());
        assertNotNull(saved);
        assertEquals("测试退款", saved.getReason());
    }

    @Test
    void testUpdateRefundStatus() {
        orderRefundService.updateRefundStatus(1L, "processing");
        OrderRefund refund = orderRefundService.getById(1L);
        assertEquals("processing", refund.getStatus());
    }

    @Test
    void testCompleteRefund() {
        orderRefundService.completeRefund(1L);
        OrderRefund refund = orderRefundService.getById(1L);
        assertEquals("completed", refund.getStatus());
        assertNotNull(refund.getRefundTime());
    }

    @Test
    void testRejectRefund() {
        orderRefundService.rejectRefund(1L, "退款金额超限");
        OrderRefund refund = orderRefundService.getById(1L);
        assertEquals("rejected", refund.getStatus());
    }

    @Test
    void testDeleteRefund() {
        orderRefundService.deleteRefund(1L);
        assertNull(orderRefundService.getById(1L));
    }

    @Test
    void testDeleteCompletedRefund() {
        // 尝试删除已完成的退款记录，应该抛出异常
        OrderRefund refund = orderRefundService.getById(1L);
        refund.setStatus("completed");
        orderRefundService.updateById(refund);

        assertThrows(BusinessException.class, () -> 
            orderRefundService.deleteRefund(1L)
        );
    }
} 