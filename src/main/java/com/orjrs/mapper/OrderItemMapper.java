package com.orjrs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orjrs.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
} 