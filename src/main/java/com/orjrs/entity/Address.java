package com.orjrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("address")
@Schema(description = "地址实体")
public class Address {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "地址ID")
    private String id;
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "收货人姓名")
    private String name;
    
    @Schema(description = "收货人手机号")
    private String phone;
    
    @Schema(description = "省份")
    private String province;
    
    @Schema(description = "城市")
    private String city;
    
    @Schema(description = "区县")
    private String district;
    
    @Schema(description = "详细地址")
    private String detail;
    
    @Schema(description = "是否默认地址")
    private Boolean isDefault;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    @TableLogic
    @Schema(description = "是否删除")
    private Boolean deleted;
} 