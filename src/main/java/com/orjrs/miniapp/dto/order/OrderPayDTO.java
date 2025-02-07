package com.orjrs.miniapp.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("订单支付参数")
public class OrderPayDTO {

    @NotNull(message = "支付方式不能为空")
    @ApiModelProperty("支付方式：1-微信支付，2-余额支付")
    private Integer payType;

    @ApiModelProperty("支付密码，余额支付时必填")
    private String payPassword;

    @NotNull(message = "支付金额不能为空")
    @ApiModelProperty("支付金额")
    private BigDecimal amount;

    @ApiModelProperty("支付备注")
    private String remark;
} 