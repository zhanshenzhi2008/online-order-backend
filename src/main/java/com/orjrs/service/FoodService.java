package com.orjrs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.entity.Food;
import java.util.List;

public interface FoodService extends IService<Food> {
    /**
     * 根据分类ID获取食品列表
     *
     * @param categoryId 分类ID
     * @return 食品列表
     */
    List<Food> getFoodsByCategory(String categoryId);

    /**
     * 获取食品详情
     *
     * @param id 食品ID
     * @return 食品详情
     */
    Food getFoodDetail(String id);

    /**
     * 获取推荐食品列表
     *
     * @return 推荐食品列表
     */
    List<Food> getRecommendFoods();

    /**
     * 搜索食品
     *
     * @param keyword 关键词
     * @return 食品列表
     */
    List<Food> searchFoods(String keyword);
} 