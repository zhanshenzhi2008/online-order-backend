package com.orjrs.utils;

import cn.dev33.satoken.stp.StpUtil;
import lombok.experimental.UtilityClass;

/**
 * 用户上下文工具类
 */
@UtilityClass
public class UserContext {

    /**
     * 获取当前登录用户ID
     */
    public String getCurrentUserId() {
        return StpUtil.getLoginIdAsString();
    }

    /**
     * 判断当前是否登录
     */
    public boolean isLogin() {
        return StpUtil.isLogin();
    }

    /**
     * 获取当前token值
     */
    public String getToken() {
        return StpUtil.getTokenValue();
    }

    /**
     * 获取当前会话剩余有效时间（单位：秒）
     */
    public long getTokenTimeout() {
        return StpUtil.getTokenTimeout();
    }

    /**
     * 获取当前会话的token信息参数
     */
    public Object getTokenInfo(String key) {
        return StpUtil.getTokenSession().get(key);
    }

    /**
     * 在当前会话中存储一个变量
     */
    public void setTokenInfo(String key, Object value) {
        StpUtil.getTokenSession().set(key, value);
    }
} 