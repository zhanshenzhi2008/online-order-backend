package com.orjrs.admin.mapper.food;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orjrs.admin.entity.food.FoodSpecification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品规格Mapper接口
 */
@Mapper
public interface FoodSpecificationMapper extends BaseMapper<FoodSpecification> {

    /**
     * 根据商品ID查询规格列表
     *
     * @param foodId 商品ID
     * @return 规格列表
     */
    @Select("SELECT * FROM food_specification WHERE food_id = #{foodId} AND deleted = 0 ORDER BY sort ASC")
    List<FoodSpecification> selectByFoodId(@Param("foodId") Long foodId);

    /**
     * 根据商品ID删除规格
     *
     * @param foodId 商品ID
     * @return 影响的行数
     */
    @Select("UPDATE food_specification SET deleted = 1 WHERE food_id = #{foodId}")
    int deleteByFoodId(@Param("foodId") Long foodId);

    /**
     * 批量插入规格
     *
     * @param specifications 规格列表
     * @return 影响的行数
     */
    int insertBatch(@Param("list") List<FoodSpecification> specifications);
} 