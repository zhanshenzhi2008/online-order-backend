package com.orjrs.admin.dto.food;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("商品创建参数")
public class FoodCreateDTO {

    @NotBlank(message = "商品名称不能为空")
    @ApiModelProperty("商品名称")
    private String name;

    @NotNull(message = "商品分类不能为空")
    @ApiModelProperty("商品分类ID")
    private Long categoryId;

    @NotNull(message = "商品价格不能为空")
    @ApiModelProperty("商品价格")
    private BigDecimal price;

    @ApiModelProperty("商品图片")
    private String image;

    @ApiModelProperty("商品描述")
    private String description;

    @ApiModelProperty("商品详情")
    private String detail;

    @ApiModelProperty("商品标签")
    private List<String> tags;

    @ApiModelProperty("商品规格")
    private List<SpecificationDTO> specifications;

    @ApiModelProperty("商品属性")
    private List<AttributeDTO> attributes;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态：0-下架，1-上架")
    private Integer status;

    @Data
    @ApiModel("规格参数")
    public static class SpecificationDTO {
        @NotBlank(message = "规格名称不能为空")
        @ApiModelProperty("规格名称")
        private String name;

        @NotNull(message = "规格价格不能为空")
        @ApiModelProperty("规格价格")
        private BigDecimal price;

        @ApiModelProperty("规格图片")
        private String image;

        @ApiModelProperty("规格描述")
        private String description;

        @ApiModelProperty("排序")
        private Integer sort;
    }

    @Data
    @ApiModel("属性参数")
    public static class AttributeDTO {
        @NotBlank(message = "属性名称不能为空")
        @ApiModelProperty("属性名称")
        private String name;

        @NotBlank(message = "属性值不能为空")
        @ApiModelProperty("属性值")
        private String value;

        @ApiModelProperty("排序")
        private Integer sort;
    }
} 