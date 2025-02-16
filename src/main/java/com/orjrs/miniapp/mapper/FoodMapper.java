package com.orjrs.miniapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orjrs.miniapp.entity.Food;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {
    /**
     * 更新商品销量
     *
     * @param foodId   商品ID
     * @param quantity 销量
     */
    @Update("UPDATE t_food SET sales = #{quantity} WHERE id = #{foodId}")
    void updateSales(@Param("foodId") String foodId, @Param("quantity") Integer quantity);
} 