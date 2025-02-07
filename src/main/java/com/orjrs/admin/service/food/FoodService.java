package com.orjrs.admin.service.food;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.admin.entity.food.Food;

import java.math.BigDecimal;

/**
 * 商品信息服务接口
 */
public interface FoodService extends IService<Food> {

    /**
     * 分页查询商品列表
     *
     * @param page 页码
     * @param size 每页大小
     * @param categoryId 分类ID，可选
     * @param name 商品名称，可选
     * @param status 状态，可选
     * @return 分页结果
     */
    IPage<Food> page(int page, int size, Long categoryId, String name, Integer status);

    /**
     * 获取商品详情
     * 包括规格和属性信息
     *
     * @param id 商品ID
     * @return 商品详情
     */
    Food getDetail(Long id);

    /**
     * 保存商品信息
     * 包括规格和属性信息
     *
     * @param food 商品信息
     */
    void saveFood(Food food);

    /**
     * 更新商品信息
     * 包括规格和属性信息
     *
     * @param food 商品信息
     */
    void updateFood(Food food);

    /**
     * 删除商品
     * 同时删除规格和属性信息
     *
     * @param id 商品ID
     */
    void deleteFood(Long id);

    /**
     * 批量更新状态
     *
     * @param ids ID列表
     * @param status 状态：0-下架，1-上架
     */
    void updateStatusBatch(Long[] ids, Integer status);

    /**
     * 更新销量
     *
     * @param foodId 商品ID
     * @param count 增加的数量，可以为负数
     */
    void updateSales(Long foodId, Integer count);

    /**
     * 更新评分
     *
     * @param foodId 商品ID
     * @param rating 评分
     */
    void updateRating(Long foodId, BigDecimal rating);
} 