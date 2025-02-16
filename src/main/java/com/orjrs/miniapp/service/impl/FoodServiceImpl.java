package com.orjrs.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.miniapp.entity.Food;
import com.orjrs.miniapp.mapper.FoodMapper;
import com.orjrs.miniapp.service.FoodService;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

    @Override
    public List<Food> getFoodsByCategory(String categoryId) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(categoryId), Food::getCategoryId, categoryId)
                .eq(Food::getStatus, "on")
                .orderByDesc(Food::getSales);
        return list(wrapper);
    }

    @Override
    public Food getFoodDetail(String id) {
        return getById(id);
    }

    @Override
    public List<Food> getRecommendFoods() {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getStatus, "on")
                .orderByDesc(Food::getSales)
                .last("limit 10");
        return list(wrapper);
    }

    @Override
    public List<Food> searchFoods(String keyword) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(keyword), Food::getName, keyword)
                .or()
                .like(StringUtils.hasText(keyword), Food::getDescription, keyword)
                .eq(Food::getStatus, "on")
                .orderByDesc(Food::getSales);
        return list(wrapper);
    }
} 