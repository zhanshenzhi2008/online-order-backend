package com.orjrs.miniapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("shop")
@Schema(description = "店铺实体")
public class Shop {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "店铺ID")
    private String id;
    
    @Schema(description = "店铺名称")
    private String name;
    
    @Schema(description = "店铺图片")
    private String image;
    
    @Schema(description = "店铺地址")
    private String address;
    
    @Schema(description = "店铺电话")
    private String phone;
    
    @Schema(description = "营业时间")
    private String businessHours;
    
    @Schema(description = "配送费")
    private BigDecimal deliveryFee;
    
    @Schema(description = "起送价")
    private BigDecimal minOrderAmount;
    
    @Schema(description = "月销量")
    private Integer monthlySales;
    
    @Schema(description = "评分")
    private BigDecimal rating;
    
    @Schema(description = "状态")
    private String status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    @TableLogic
    @Schema(description = "是否删除")
    private Boolean deleted;
} 