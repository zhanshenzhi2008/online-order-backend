package com.orjrs.admin.vo.food;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("商品分类树形结构")
public class CategoryTreeVO {

    @ApiModelProperty("分类ID")
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

    @ApiModelProperty("子分类")
    private List<CategoryTreeVO> children;
} 