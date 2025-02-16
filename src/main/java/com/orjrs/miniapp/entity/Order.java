package com.orjrs.miniapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@Schema(description = "订单信息")
@TableName("order")
public class Order {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "订单ID")
    private String id;
    
    @Schema(description = "订单号")
    private String orderNo;
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "商店ID")
    private String shopId;
    
    @Schema(description = "地址ID")
    private String addressId;
    
    @Schema(description = "总金额")
    private BigDecimal totalAmount;
    
    @Schema(description = "支付金额")
    private BigDecimal payAmount;
    
    @Schema(description = "订单状态")
    private String status;
    
    @Schema(description = "状态文本")
    private String statusText;
    
    @Schema(description = "支付方式")
    private String paymentMethod;
    
    @Schema(description = "支付时间")
    private LocalDateTime payTime;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    @TableLogic
    @Schema(description = "是否删除")
    private Boolean deleted;
} 