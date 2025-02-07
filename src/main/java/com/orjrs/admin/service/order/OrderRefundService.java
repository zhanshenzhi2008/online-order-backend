package com.orjrs.admin.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.admin.entity.order.OrderRefund;

public interface OrderRefundService extends IService<OrderRefund> {
    
    /**
     * 分页查询退款列表
     */
    IPage<OrderRefund> page(int page, int size, Long orderId, String status, String reason);
    
    /**
     * 获取退款详情
     */
    OrderRefund getDetail(Long id);
    
    /**
     * 创建退款申请
     */
    void createRefund(OrderRefund refund);
    
    /**
     * 更新退款状态
     */
    void updateRefundStatus(Long id, String status);
    
    /**
     * 完成退款
     */
    void completeRefund(Long id);
    
    /**
     * 拒绝退款
     */
    void rejectRefund(Long id, String reason);
    
    /**
     * 删除退款记录
     */
    void deleteRefund(Long id);
} 