package com.orjrs.admin.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.admin.entity.order.Order;
import com.orjrs.admin.entity.order.OrderStatistics;
import com.orjrs.admin.mapper.order.OrderMapper;
import com.orjrs.admin.mapper.order.OrderStatisticsMapper;
import com.orjrs.admin.service.order.OrderStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStatisticsServiceImpl extends ServiceImpl<OrderStatisticsMapper, OrderStatistics> implements OrderStatisticsService {

    private final OrderMapper orderMapper;

    @Override
    public List<OrderStatistics> getStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<OrderStatistics> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(OrderStatistics::getDate, startTime.toLocalDate(), endTime.toLocalDate())
                .orderByAsc(OrderStatistics::getDate);
        return list(wrapper);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨执行
    @Transactional(rollbackFor = Exception.class)
    public void generateDailyStatistics() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startTime = LocalDateTime.of(yesterday, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(yesterday, LocalTime.MAX);

        // 查询昨天的订单数据
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(Order::getCreateTime, startTime, endTime);
        List<Order> orders = orderMapper.selectList(wrapper);

        // 统计数据
        OrderStatistics statistics = new OrderStatistics();
        statistics.setDate(yesterday);
        statistics.setTotalOrders(orders.size());
        statistics.setTotalAmount(orders.stream()
                .map(Order::getActualAmount)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
        statistics.setCompletedOrders((int) orders.stream()
                .filter(order -> "completed".equals(order.getStatus()))
                .count());
        statistics.setCancelledOrders((int) orders.stream()
                .filter(order -> "cancelled".equals(order.getStatus()))
                .count());
        statistics.setRefundedOrders((int) orders.stream()
                .filter(order -> "refunded".equals(order.getStatus()))
                .count());

        // 保存统计数据
        save(statistics);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatistics(Order order) {
        LocalDate orderDate = order.getCreateTime().toLocalDate();
        OrderStatistics statistics = getOne(new LambdaQueryWrapper<OrderStatistics>()
                .eq(OrderStatistics::getDate, orderDate));

        if (statistics == null) {
            statistics = new OrderStatistics();
            statistics.setDate(orderDate);
            statistics.setTotalOrders(1);
            statistics.setTotalAmount(order.getActualAmount());
        } else {
            statistics.setTotalOrders(statistics.getTotalOrders() + 1);
            statistics.setTotalAmount(statistics.getTotalAmount().add(order.getActualAmount()));
        }

        // 更新对应状态的订单数
        switch (order.getStatus()) {
            case "completed":
                statistics.setCompletedOrders(statistics.getCompletedOrders() + 1);
                break;
            case "cancelled":
                statistics.setCancelledOrders(statistics.getCancelledOrders() + 1);
                break;
            case "refunded":
                statistics.setRefundedOrders(statistics.getRefundedOrders() + 1);
                break;
        }

        saveOrUpdate(statistics);
    }
} 