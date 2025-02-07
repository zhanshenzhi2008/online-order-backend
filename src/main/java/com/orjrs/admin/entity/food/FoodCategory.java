package com.orjrs.admin.entity.food;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类实体
 */
@Data
@ApiModel("商品分类")
@TableName("food_category")
public class FoodCategory {

    @ApiModelProperty("分类ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("父级ID")
    private Long parentId;

    @ApiModelProperty("分类图标")
    private String icon;

    @ApiModelProperty("分类图片")
    private String image;

    @ApiModelProperty("分类描述")
    private String description;

    @ApiModelProperty("商品数量")
    private Integer foodCount;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态：0-禁用，1-启用")
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

    @ApiModelProperty("子分类列表")
    @TableField(exist = false)
    private List<FoodCategory> children;
} 