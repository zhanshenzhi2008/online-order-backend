package com.orjrs.miniapp.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("小程序注册参数")
public class MiniAppRegisterDTO {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty("手机号")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String code;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("性别")
    private Integer gender;
} 