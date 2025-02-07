package com.orjrs.admin.entity.order;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("order_statistics")
@ApiModel(description = "订单统计信息")
public class OrderStatistics {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("统计ID")
    private Long id;

    @ApiModelProperty("统计日期")
    private LocalDate date;

    @ApiModelProperty("订单总数")
    private Integer totalOrders;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("已完成订单数")
    private Integer completedOrders;

    @ApiModelProperty("已取消订单数")
    private Integer cancelledOrders;

    @ApiModelProperty("已退款订单数")
    private Integer refundedOrders;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
} 