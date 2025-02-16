package com.orjrs.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体类
 */
@Data
@TableName(value = "user", autoResultMap = true)
@Schema(description = "用户实体")
public class User {
    /**
     * 用户ID
     */
    @TableId
    @Schema(description = "用户ID")
    private String id;
    
    /**
     * 微信openId
     */
    @Schema(description = "微信openId")
    private String openId;
    
    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;
    
    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;
    
    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;
    
    /**
     * 性别
     */
    @Schema(description = "性别")
    private String gender;
    
    /**
     * 状态
     */
    @Schema(description = "状态")
    private String status;
    
    /**
     * 角色列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "角色列表")
    private List<String> roles;
    
    /**
     * 权限列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "权限列表")
    private List<String> permissions;
    
    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;
    
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    @Schema(description = "是否删除")
    private Boolean deleted;

    /**
     * token
     */
    @TableField(exist = false)
    @Schema(description = "token")
    private String token;

    public void setToken(String token) {
        this.token = token;
    }
} 