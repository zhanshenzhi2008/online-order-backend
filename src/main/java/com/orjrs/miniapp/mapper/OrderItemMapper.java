package com.orjrs.miniapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orjrs.miniapp.entity.OrderItem;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
} 