package com.orjrs.miniapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orjrs.miniapp.entity.Category;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
} 