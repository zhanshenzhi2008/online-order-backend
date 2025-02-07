package com.orjrs.miniapp.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("小程序登录参数")
public class MiniAppLoginDTO {

    @NotBlank(message = "code不能为空")
    @ApiModelProperty("小程序code")
    private String code;

    @ApiModelProperty("用户信息")
    private UserInfo userInfo;

    @Data
    public static class UserInfo {
        @ApiModelProperty("昵称")
        private String nickName;

        @ApiModelProperty("头像")
        private String avatarUrl;

        @ApiModelProperty("性别")
        private Integer gender;
    }
} 