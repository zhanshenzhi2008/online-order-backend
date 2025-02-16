package com.orjrs.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.miniapp.entity.Order;
import com.orjrs.miniapp.mapper.FoodMapper;
import com.orjrs.miniapp.mapper.OrderMapper;
import com.orjrs.miniapp.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    private final FoodMapper foodMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        // 设置订单号
        order.setOrderNo("ORDER" + System.currentTimeMillis());
        // 设置订单状态
        order.setStatus("pending");
        order.setStatusText("待付款");
        // 设置创建时间
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        // 保存订单
        save(order);
        return order;
    }

    @Override
    public List<Order> getOrderList(String userId, String status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId)
                .eq(StringUtils.hasText(status), Order::getStatus, status)
                .orderByDesc(Order::getCreateTime);
        return list(wrapper);
    }

    @Override
    public Order getOrderDetail(String id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order cancelOrder(String id) {
        Order order = getById(id);
        if (order != null && "pending".equals(order.getStatus())) {
            order.setStatus("cancelled");
            order.setStatusText("已取消");
            order.setUpdateTime(LocalDateTime.now());
            updateById(order);
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order payOrder(String id, String paymentMethod) {
        Order order = getById(id);
        if (order != null && "pending".equals(order.getStatus())) {
            order.setStatus("paid");
            order.setStatusText("待发货");
            order.setPaymentMethod(paymentMethod);
            order.setPayTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            updateById(order);
        }
        return order;
    }

    @Override
    public void updateFoodSales() {
        // 查询所有已完成订单的商品销量
        List<Map<String, Object>> salesList = baseMapper.selectFoodSales();

        // 更新商品销量
        for (Map<String, Object> sales : salesList) {
            String foodId = (String) sales.get("food_id");
            Integer quantity = ((Number) sales.get("total_quantity")).intValue();
            foodMapper.updateSales(foodId, quantity);
        }
    }
} 