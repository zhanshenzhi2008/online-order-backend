package com.orjrs.admin.vo.food;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("商品信息")
public class FoodVO {

    @ApiModelProperty("商品ID")
    private Long id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品分类ID")
    private Long categoryId;

    @ApiModelProperty("商品分类名称")
    private String categoryName;

    @ApiModelProperty("商品价格")
    private BigDecimal price;

    @ApiModelProperty("商品图片")
    private String image;

    @ApiModelProperty("商品描述")
    private String description;

    @ApiModelProperty("商品标签")
    private List<String> tags;

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

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
} 