package com.orjrs.admin.entity.food;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品规格实体
 */
@Data
@ApiModel("商品规格")
@TableName("food_specification")
public class FoodSpecification {

    @ApiModelProperty("规格ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品ID")
    private Long foodId;

    @ApiModelProperty("规格名称")
    private String name;

    @ApiModelProperty("规格价格")
    private BigDecimal price;

    @ApiModelProperty("规格图片")
    private String image;

    @ApiModelProperty("规格描述")
    private String description;

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