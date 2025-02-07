package com.orjrs.admin.mapper.food;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orjrs.admin.entity.food.FoodCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 商品分类Mapper接口
 */
@Mapper
public interface FoodCategoryMapper extends BaseMapper<FoodCategory> {

    /**
     * 更新商品数量
     *
     * @param categoryId 分类ID
     * @param count 增加的数量，可以为负数
     * @return 影响的行数
     */
    @Update("UPDATE food_category SET food_count = food_count + #{count} WHERE id = #{categoryId}")
    int updateFoodCount(@Param("categoryId") Long categoryId, @Param("count") Integer count);

    /**
     * 批量更新状态
     *
     * @param ids ID列表
     * @param status 状态
     * @return 影响的行数
     */
    @Update("<script>" +
            "UPDATE food_category SET status = #{status} " +
            "WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int updateStatusBatch(@Param("ids") Long[] ids, @Param("status") Integer status);
} 