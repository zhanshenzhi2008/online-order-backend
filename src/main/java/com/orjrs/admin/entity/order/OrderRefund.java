package com.orjrs.admin.entity.order;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_refund")
@ApiModel(description = "订单退款信息")
public class OrderRefund {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("退款ID")
    private Long id;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("退款金额")
    private BigDecimal amount;

    @ApiModelProperty("退款原因")
    private String reason;

    @ApiModelProperty("退款状态")
    private String status;

    @ApiModelProperty("退款时间")
    private LocalDateTime refundTime;

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