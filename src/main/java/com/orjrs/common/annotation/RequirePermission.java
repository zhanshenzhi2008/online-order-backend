package com.orjrs.common.annotation;

import com.orjrs.common.enums.UserPermission;

import java.lang.annotation.*;

/**
 * 权限注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    /**
     * 需要的权限
     */
    UserPermission[] value();

    /**
     * 是否需要全部权限，默认任一权限即可
     */
    boolean all() default false;
} 