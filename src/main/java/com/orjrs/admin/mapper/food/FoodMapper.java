package com.orjrs.admin.mapper.food;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orjrs.admin.entity.food.Food;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 商品信息Mapper接口
 */
@Mapper
public interface FoodMapper extends BaseMapper<Food> {

    /**
     * 更新销量
     *
     * @param foodId 商品ID
     * @param count 增加的数量，可以为负数
     * @return 影响的行数
     */
    @Update("UPDATE food SET sales = sales + #{count} WHERE id = #{foodId}")
    int updateSales(@Param("foodId") Long foodId, @Param("count") Integer count);

    /**
     * 更新评分和评价数
     *
     * @param foodId 商品ID
     * @param rating 评分
     * @return 影响的行数
     */
    @Update("UPDATE food SET rating = (rating * comment_count + #{rating}) / (comment_count + 1), " +
            "comment_count = comment_count + 1 WHERE id = #{foodId}")
    int updateRating(@Param("foodId") Long foodId, @Param("rating") BigDecimal rating);

    /**
     * 批量更新状态
     *
     * @param ids ID列表
     * @param status 状态
     * @return 影响的行数
     */
    @Update("<script>" +
            "UPDATE food SET status = #{status} " +
            "WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int updateStatusBatch(@Param("ids") Long[] ids, @Param("status") Integer status);

    /**
     * 根据分类ID统计商品数量
     *
     * @param categoryId 分类ID
     * @return 商品数量
     */
    @Select("SELECT COUNT(*) FROM food WHERE category_id = #{categoryId} AND deleted = 0")
    int countByCategory(@Param("categoryId") Long categoryId);
} 
