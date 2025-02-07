package com.orjrs.miniapp.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("小程序用户信息更新参数")
public class MiniAppUserUpdateDTO {

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("生日")
    private String birthday;

    @ApiModelProperty("个性签名")
    private String signature;
} 