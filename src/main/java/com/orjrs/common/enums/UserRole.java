package com.orjrs.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色枚举
 */
@Getter
@AllArgsConstructor
public enum UserRole {
    ADMIN("admin", "管理员"),
    MERCHANT("merchant", "商家"),
    USER("user", "普通用户");

    private final String code;
    private final String desc;
} 