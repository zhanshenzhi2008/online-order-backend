package com.orjrs.admin.entity.order;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order")
@ApiModel(description = "订单信息")
public class Order {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("订单ID")
    private Long id;

    @ApiModelProperty("用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @ApiModelProperty("店铺ID")
    @NotNull(message = "店铺ID不能为空")
    private Long shopId;

    @ApiModelProperty("订单编号")
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;

    @ApiModelProperty("订单状态")
    private String status;

    @ApiModelProperty("商品总价")
    @NotNull(message = "商品总价不能为空")
    private BigDecimal totalPrice;

    @ApiModelProperty("包装费")
    private BigDecimal packingFee;

    @ApiModelProperty("配送费")
    private BigDecimal deliveryFee;

    @ApiModelProperty("实付金额")
    @NotNull(message = "实付金额不能为空")
    private BigDecimal actualAmount;

    @ApiModelProperty("支付方式")
    private String paymentMethod;

    @ApiModelProperty("支付时间")
    private LocalDateTime paymentTime;

    @ApiModelProperty("配送地址ID")
    @NotNull(message = "配送地址不能为空")
    private Long addressId;

    @ApiModelProperty("配送时间")
    private LocalDateTime deliveryTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @ApiModelProperty("是否删除")
    private Boolean deleted;
} 