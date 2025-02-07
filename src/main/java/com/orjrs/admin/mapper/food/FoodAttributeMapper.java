package com.orjrs.admin.mapper.food;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orjrs.admin.entity.food.FoodAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品属性Mapper接口
 */
@Mapper
public interface FoodAttributeMapper extends BaseMapper<FoodAttribute> {

    /**
     * 根据商品ID查询属性列表
     *
     * @param foodId 商品ID
     * @return 属性列表
     */
    @Select("SELECT * FROM food_attribute WHERE food_id = #{foodId} AND deleted = 0 ORDER BY sort ASC")
    List<FoodAttribute> selectByFoodId(@Param("foodId") Long foodId);

    /**
     * 根据商品ID删除属性
     *
     * @param foodId 商品ID
     * @return 影响的行数
     */
    @Select("UPDATE food_attribute SET deleted = 1 WHERE food_id = #{foodId}")
    int deleteByFoodId(@Param("foodId") Long foodId);

    /**
     * 批量插入属性
     *
     * @param attributes 属性列表
     * @return 影响的行数
     */
    int insertBatch(@Param("list") List<FoodAttribute> attributes);
} 