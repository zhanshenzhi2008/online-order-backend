package com.orjrs.admin.dto.food;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("商品分类创建参数")
public class CategoryCreateDTO {

    @NotBlank(message = "分类名称不能为空")
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

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;
} 