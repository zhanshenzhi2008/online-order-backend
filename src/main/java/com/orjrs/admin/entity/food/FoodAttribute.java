package com.orjrs.admin.entity.food;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品属性实体
 */
@Data
@ApiModel("商品属性")
@TableName("food_attribute")
public class FoodAttribute {

    @ApiModelProperty("属性ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品ID")
    private Long foodId;

    @ApiModelProperty("属性名称")
    private String name;

    @ApiModelProperty("属性值")
    private String value;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否删除：0-未删除，1-已删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
} 