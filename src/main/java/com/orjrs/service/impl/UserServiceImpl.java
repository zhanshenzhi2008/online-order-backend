package com.orjrs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.common.BaseException;
import com.orjrs.entity.User;
import com.orjrs.mapper.UserMapper;
import com.orjrs.service.UserService;
import com.orjrs.utils.RedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final RedisUtils redisUtils;
    private static final String USER_SETTINGS_KEY = "user:settings:";

    public UserServiceImpl(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User login(String openId) {
        // 查询用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenId, openId);
        User user = getOne(wrapper);

        // 用户不存在则创建新用户
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            user.setStatus("active");
            user.setLastLoginTime(LocalDateTime.now());
            save(user);
        } else {
            // 更新最后登录时间
            user.setLastLoginTime(LocalDateTime.now());
            updateById(user);
        }

        return user;
    }

    @Override
    public User getUserInfo(String userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUserInfo(User user) {
        if (user.getId() == null) {
            throw new BaseException("用户ID不能为空");
        }

        // 检查用户是否存在
        User existUser = getById(user.getId());
        if (existUser == null) {
            throw new BaseException("用户不存在");
        }

        // 更新用户信息
        updateById(user);
        return getById(user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updatePhone(String userId, String phone) {
        User user = getById(userId);
        if (user == null) {
            throw new BaseException("用户不存在");
        }

        user.setPhone(phone);
        updateById(user);
        return user;
    }

    @Override
    public Object getSettings(String userId) {
        // 从Redis中获取用户设置
        String key = USER_SETTINGS_KEY + userId;
        Object settings = redisUtils.get(key);
        if (settings == null) {
            // 如果Redis中没有，则返回默认设置
            Map<String, Object> defaultSettings = new HashMap<>();
            defaultSettings.put("pushNotification", true);
            defaultSettings.put("soundEnabled", true);
            defaultSettings.put("language", "zh_CN");
            // 将默认设置存入Redis
            redisUtils.set(key, defaultSettings, 7, TimeUnit.DAYS);
            return defaultSettings;
        }
        return settings;
    }

    @Override
    public Object updateSettings(String userId, Object settings) {
        // 更新Redis中的用户设置
        String key = USER_SETTINGS_KEY + userId;
        redisUtils.set(key, settings, 7, TimeUnit.DAYS);
        return settings;
    }
} 