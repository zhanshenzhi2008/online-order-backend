package com.orjrs.miniapp.entity;

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
import java.util.List;
import java.util.Map;

@Data
@TableName(value = "food", autoResultMap = true)
@Schema(description = "食品实体")
public class Food {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "食品ID")
    private String id;
    
    @Schema(description = "店铺ID")
    private String shopId;
    
    @Schema(description = "分类ID")
    private String categoryId;
    
    @Schema(description = "食品名称")
    private String name;
    
    @Schema(description = "食品图片")
    private String image;
    
    @Schema(description = "食品描述")
    private String description;
    
    @Schema(description = "原价")
    private BigDecimal originalPrice;
    
    @Schema(description = "现价")
    private BigDecimal price;
    
    @Schema(description = "包装费")
    private BigDecimal packingFee;
    
    @Schema(description = "销量")
    private Integer sales;
    
    @Schema(description = "库存")
    private Integer stock;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "规格选项")
    private List<Map<String, Object>> specs;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "加料选项")
    private List<Map<String, Object>> additions;
    
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