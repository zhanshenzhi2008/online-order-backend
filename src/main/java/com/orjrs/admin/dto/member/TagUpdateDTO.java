package com.orjrs.admin.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("会员标签更新参数")
public class TagUpdateDTO {

    @NotBlank(message = "标签名称不能为空")
    @ApiModelProperty("标签名称")
    private String name;

    @ApiModelProperty("标签颜色")
    private String color;

    @ApiModelProperty("标签图标")
    private String icon;

    @ApiModelProperty("标签描述")
    private String description;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;
} 