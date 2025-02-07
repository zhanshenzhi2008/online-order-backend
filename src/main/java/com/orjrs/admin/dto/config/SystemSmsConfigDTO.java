package com.orjrs.admin.dto.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("系统短信配置参数")
public class SystemSmsConfigDTO {

    @NotBlank(message = "短信服务商不能为空")
    @ApiModelProperty("短信服务商：aliyun-阿里云，tencent-腾讯云")
    private String provider;

    @NotBlank(message = "AccessKey不能为空")
    @ApiModelProperty("AccessKey")
    private String accessKey;

    @NotBlank(message = "AccessSecret不能为空")
    @ApiModelProperty("AccessSecret")
    private String accessSecret;

    @ApiModelProperty("短信签名")
    private String signName;

    @ApiModelProperty("短信模板")
    private String templateCode;

    @ApiModelProperty("是否启用")
    private Boolean enabled;
} 