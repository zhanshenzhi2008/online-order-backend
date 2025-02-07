package com.orjrs.admin.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.admin.entity.order.OrderRefund;
import com.orjrs.admin.mapper.order.OrderRefundMapper;
import com.orjrs.admin.service.order.OrderRefundService;
import com.orjrs.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRefundServiceImpl extends ServiceImpl<OrderRefundMapper, OrderRefund> implements OrderRefundService {

    @Override
    public IPage<OrderRefund> page(int page, int size, Long orderId, String status, String reason) {
        LambdaQueryWrapper<OrderRefund> wrapper = new LambdaQueryWrapper<>();
        
        // 设置查询条件
        wrapper.eq(orderId != null, OrderRefund::getOrderId, orderId)
                .eq(StringUtils.isNotBlank(status), OrderRefund::getStatus, status)
                .like(StringUtils.isNotBlank(reason), OrderRefund::getReason, reason)
                .orderByDesc(OrderRefund::getCreateTime);
        
        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public OrderRefund getDetail(Long id) {
        OrderRefund refund = getById(id);
        if (refund == null) {
            throw new BusinessException("退款记录不存在");
        }
        return refund;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRefund(OrderRefund refund) {
        // 设置初始状态
        refund.setStatus("pending");
        save(refund);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRefundStatus(Long id, String status) {
        OrderRefund refund = getDetail(id);
        refund.setStatus(status);
        updateById(refund);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeRefund(Long id) {
        OrderRefund refund = getDetail(id);
        refund.setStatus("completed");
        refund.setRefundTime(LocalDateTime.now());
        updateById(refund);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectRefund(Long id, String reason) {
        OrderRefund refund = getDetail(id);
        refund.setStatus("rejected");
        refund.setReason(reason);
        updateById(refund);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRefund(Long id) {
        OrderRefund refund = getDetail(id);
        if ("completed".equals(refund.getStatus())) {
            throw new BusinessException("已完成的退款记录不能删除");
        }
        removeById(id);
    }
} 