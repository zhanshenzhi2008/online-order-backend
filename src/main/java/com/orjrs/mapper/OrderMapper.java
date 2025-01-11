package com.orjrs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orjrs.entity.Order;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseMapper<Order> {
    /**
     * 查询商品销量统计
     */
    @Select("SELECT oi.food_id, SUM(oi.quantity) as total_quantity " +
            "FROM t_order_item oi " +
            "LEFT JOIN t_order o ON oi.order_id = o.id " +
            "WHERE o.status = 'completed' " +
            "GROUP BY oi.food_id")
    List<Map<String, Object>> selectFoodSales();
} 