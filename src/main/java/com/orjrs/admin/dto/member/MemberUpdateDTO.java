package com.orjrs.admin.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@ApiModel("会员更新参数")
public class MemberUpdateDTO {

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("性别：0-未知，1-男，2-女")
    private Integer gender;

    @ApiModelProperty("生日")
    private String birthday;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("会员等级ID")
    private Long levelId;

    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;

    @ApiModelProperty("标签ID列表")
    private List<Long> tagIds;

    @ApiModelProperty("备注")
    private String remark;
} 