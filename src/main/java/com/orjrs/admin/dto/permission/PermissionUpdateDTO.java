package com.orjrs.admin.dto.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("权限更新参数")
public class PermissionUpdateDTO {

    @NotBlank(message = "权限名称不能为空")
    @ApiModelProperty("权限名称")
    private String name;

    @NotNull(message = "权限类型不能为空")
    @ApiModelProperty("权限类型：1-目录，2-菜单，3-按钮")
    private Integer type;

    @ApiModelProperty("父级ID")
    private Long parentId;

    @ApiModelProperty("路由路径")
    private String path;

    @ApiModelProperty("组件路径")
    private String component;

    @ApiModelProperty("权限标识")
    private String permission;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否外链")
    private Boolean external;

    @ApiModelProperty("是否缓存")
    private Boolean cache;

    @ApiModelProperty("是否显示")
    private Boolean visible;

    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;
} 