package com.orjrs.admin.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.admin.entity.order.Order;
import com.orjrs.admin.entity.order.OrderStatistics;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderStatisticsService extends IService<OrderStatistics> {

    /**
     * 获取订单统计数据
     */
    List<OrderStatistics> getStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 生成每日统计数据
     */
    void generateDailyStatistics();

    /**
     * 更新统计数据
     */
    void updateStatistics(Order order);
} 