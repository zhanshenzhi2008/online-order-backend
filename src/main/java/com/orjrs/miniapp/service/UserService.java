package com.orjrs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param openId 微信openId
     * @return 用户信息
     */
    User login(String openId);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserInfo(String userId);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 更新后的用户信息
     */
    User updateUserInfo(User user);

    /**
     * 更新手机号
     *
     * @param userId 用户ID
     * @param phone  手机号
     * @return 更新后的用户信息
     */
    User updatePhone(String userId, String phone);

    /**
     * 获取用户设置
     *
     * @param userId 用户ID
     * @return 用户设置
     */
    Object getSettings(String userId);

    /**
     * 更新用户设置
     *
     * @param userId   用户ID
     * @param settings 用户设置
     * @return 更新后的用户设置
     */
    Object updateSettings(String userId, Object settings);
} 