package com.orjrs.admin.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.admin.entity.order.Order;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public interface OrderService extends IService<Order> {
    
    /**
     * 分页查询订单列表
     */
    IPage<Order> page(int page, int size, String status, Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取订单详情
     */
    Order getDetail(Long id);
    
    /**
     * 取消订单
     */
    void cancelOrder(Long id, String reason);
    
    /**
     * 退款订单
     */
    void refundOrder(Long id, String reason);
    
    /**
     * 导出订单
     */
    void exportOrders(String status, Long userId, LocalDateTime startTime, LocalDateTime endTime, HttpServletResponse response);

    /**
     * 创建订单（带库存检查）
     * @param order 订单信息
     * @param goodsId 商品ID
     * @param quantity 购买数量
     * @return 订单ID
     */
    Long createOrderWithStockCheck(Order order, Long goodsId, Integer quantity);
} 