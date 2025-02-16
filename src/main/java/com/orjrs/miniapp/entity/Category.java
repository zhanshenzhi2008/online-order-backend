package com.orjrs.miniapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("category")
@Schema(description = "分类实体")
public class Category {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "分类ID")
    private String id;
    
    @Schema(description = "店铺ID")
    private String shopId;
    
    @Schema(description = "分类名称")
    private String name;
    
    @Schema(description = "分类图标")
    private String icon;
    
    @Schema(description = "排序")
    private Integer sort;
    
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