package com.orjrs.miniapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orjrs.miniapp.entity.Cart;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
} 