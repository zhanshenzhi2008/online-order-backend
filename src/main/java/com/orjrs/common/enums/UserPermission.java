package com.orjrs.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户权限枚举
 */
@Getter
@AllArgsConstructor
public enum UserPermission {
    // 用户管理权限
    USER_VIEW("user:view", "查看用户"),
    USER_ADD("user:add", "添加用户"),
    USER_EDIT("user:edit", "编辑用户"),
    USER_DELETE("user:delete", "删除用户"),

    // 商品管理权限
    FOOD_VIEW("food:view", "查看商品"),
    FOOD_ADD("food:add", "添加商品"),
    FOOD_EDIT("food:edit", "编辑商品"),
    FOOD_DELETE("food:delete", "删除商品"),

    // 订单管理权限
    ORDER_VIEW("order:view", "查看订单"),
    ORDER_EDIT("order:edit", "编辑订单"),
    ORDER_DELETE("order:delete", "删除订单"),

    // 店铺管理权限
    SHOP_VIEW("shop:view", "查看店铺"),
    SHOP_ADD("shop:add", "添加店铺"),
    SHOP_EDIT("shop:edit", "编辑店铺"),
    SHOP_DELETE("shop:delete", "删除店铺");

    private final String code;
    private final String desc;
} 