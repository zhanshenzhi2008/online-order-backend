package com.orjrs.admin.entity.food;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品信息实体
 */
@Data
@ApiModel("商品信息")
@TableName("food")
public class Food {

    @ApiModelProperty("商品ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("商品价格")
    private BigDecimal price;

    @ApiModelProperty("商品图片")
    private String image;

    @ApiModelProperty("商品描述")
    private String description;

    @ApiModelProperty("商品详情")
    private String detail;

    @ApiModelProperty("商品标签，多个标签用逗号分隔")
    private String tags;

    @ApiModelProperty("销量")
    private Integer sales;

    @ApiModelProperty("评分")
    private BigDecimal rating;

    @ApiModelProperty("评价数量")
    private Integer commentCount;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态：0-下架，1-上架")
    private Integer status;

    @ApiModelProperty("是否删除：0-未删除，1-已删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("商品规格列表")
    @TableField(exist = false)
    private List<FoodSpecification> specifications;

    @ApiModelProperty("商品属性列表")
    @TableField(exist = false)
    private List<FoodAttribute> attributes;
} 