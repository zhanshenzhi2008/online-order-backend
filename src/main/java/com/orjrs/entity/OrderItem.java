package com.orjrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@TableName(value = "order_item", autoResultMap = true)
@Schema(description = "订单项实体")
public class OrderItem {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "订单项ID")
    private String id;
    
    @Schema(description = "订单ID")
    private String orderId;
    
    @Schema(description = "食品ID")
    private String foodId;
    
    @Schema(description = "食品名称")
    private String foodName;
    
    @Schema(description = "食品图片")
    private String foodImage;
    
    @Schema(description = "食品价格")
    private BigDecimal price;
    
    @Schema(description = "购买数量")
    private Integer quantity;
    
    @Schema(description = "小计金额")
    private BigDecimal subtotal;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "规格信息")
    private Map<String, Object> specs;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "加料信息")
    private Map<String, Object> additions;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "是否删除")
    private Boolean deleted;
} 