package com.orjrs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.entity.Order;
import java.util.List;

public interface OrderService extends IService<Order> {
    /**
     * 创建订单
     *
     * @param order 订单信息
     * @return 创建的订单
     */
    Order createOrder(Order order);

    /**
     * 获取订单列表
     *
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> getOrderList(String userId, String status);

    /**
     * 获取订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    Order getOrderDetail(String id);

    /**
     * 取消订单
     *
     * @param id 订单ID
     * @return 取消后的订单
     */
    Order cancelOrder(String id);

    /**
     * 支付订单
     *
     * @param id 订单ID
     * @param paymentMethod 支付方式
     * @return 支付后的订单
     */
    Order payOrder(String id, String paymentMethod);

    /**
     * 更新商品销量统计
     */
    void updateFoodSales();
} 