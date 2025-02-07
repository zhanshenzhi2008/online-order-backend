package com.orjrs.admin.entity.goods;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("goods")
@ApiModel(description = "商品信息")
public class Goods {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("商品ID")
    private Long id;

    @ApiModelProperty("店铺ID")
    @NotNull(message = "店铺ID不能为空")
    private Long shopId;

    @ApiModelProperty("分类ID")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @ApiModelProperty("商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty("商品图片")
    private String image;

    @ApiModelProperty("商品描述")
    private String description;

    @ApiModelProperty("原价")
    @NotNull(message = "原价不能为空")
    @DecimalMin(value = "0.01", message = "原价必须大于0")
    private BigDecimal originalPrice;

    @ApiModelProperty("现价")
    @NotNull(message = "现价不能为空")
    @DecimalMin(value = "0.01", message = "现价必须大于0")
    private BigDecimal price;

    @ApiModelProperty("包装费")
    @DecimalMin(value = "0", message = "包装费不能小于0")
    private BigDecimal packingFee;

    @ApiModelProperty("销量")
    private Integer sales;

    @ApiModelProperty("库存")
    @Min(value = 0, message = "库存不能小于0")
    private Integer stock;

    @ApiModelProperty("规格选项")
    private String specs;

    @ApiModelProperty("加料选项")
    private String additions;

    @ApiModelProperty("状态：0-下架，1-上架")
    private Integer status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @ApiModelProperty("是否删除")
    private Boolean deleted;
} 