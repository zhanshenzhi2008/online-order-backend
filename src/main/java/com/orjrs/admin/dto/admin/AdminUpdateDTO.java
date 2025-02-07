package com.orjrs.admin.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@ApiModel("管理员更新参数")
public class AdminUpdateDTO {

    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty("姓名")
    private String name;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("角色ID列表")
    private List<Long> roleIds;
} 