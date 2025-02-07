package com.orjrs.admin.dto.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel("角色更新参数")
public class RoleUpdateDTO {

    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色描述")
    private String description;

    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("权限ID列表")
    private List<Long> permissionIds;
} 