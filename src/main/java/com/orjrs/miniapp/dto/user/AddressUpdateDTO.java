package com.orjrs.miniapp.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("收货地址更新参数")
public class AddressUpdateDTO {

    @NotBlank(message = "收货人不能为空")
    @ApiModelProperty("收货人")
    private String receiver;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty("手机号")
    private String phone;

    @NotBlank(message = "省份不能为空")
    @ApiModelProperty("省份")
    private String province;

    @NotBlank(message = "城市不能为空")
    @ApiModelProperty("城市")
    private String city;

    @NotBlank(message = "区县不能为空")
    @ApiModelProperty("区县")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    @ApiModelProperty("详细地址")
    private String detail;

    @ApiModelProperty("是否默认地址")
    private Boolean isDefault;

    @ApiModelProperty("标签")
    private String tag;
} 