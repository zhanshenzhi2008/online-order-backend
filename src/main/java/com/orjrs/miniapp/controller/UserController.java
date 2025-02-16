package com.orjrs.miniapp.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.orjrs.common.R;
import com.orjrs.common.annotation.RequirePermission;
import com.orjrs.common.enums.UserPermission;
import com.orjrs.miniapp.entity.User;
import com.orjrs.miniapp.service.UserService;
import com.orjrs.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public R<User> login(@Parameter(description = "微信openId") @RequestParam String openId) {
        User user = userService.login(openId);
        // 登录成功，使用Sa-Token记录用户会话
        StpUtil.login(user.getId());
        // 获取token
        String token = UserContext.getToken();
        user.setToken(token);
        return R.success(user);
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    @RequirePermission(UserPermission.USER_VIEW)
    public R<User> getUserInfo() {
        String userId = UserContext.getCurrentUserId();
        User user = userService.getUserInfo(userId);
        return R.success(user);
    }

    @Operation(summary = "更新用户信息")
    @PostMapping("/update")
    @RequirePermission(UserPermission.USER_EDIT)
    public R<User> updateUserInfo(@RequestBody User user) {
        String userId = UserContext.getCurrentUserId();
        user.setId(userId);
        User updatedUser = userService.updateUserInfo(user);
        return R.success(updatedUser);
    }

    @Operation(summary = "更新手机号")
    @PostMapping("/phone")
    @RequirePermission(UserPermission.USER_EDIT)
    public R<User> updatePhone(@Parameter(description = "手机号") @RequestParam String phone) {
        String userId = UserContext.getCurrentUserId();
        User user = userService.updatePhone(userId, phone);
        return R.success(user);
    }

    @Operation(summary = "获取用户设置")
    @GetMapping("/settings")
    @RequirePermission(UserPermission.USER_VIEW)
    public R<Object> getSettings() {
        String userId = UserContext.getCurrentUserId();
        Object settings = userService.getSettings(userId);
        return R.success(settings);
    }

    @Operation(summary = "更新用户设置")
    @PostMapping("/settings")
    @RequirePermission(UserPermission.USER_EDIT)
    public R<Object> updateSettings(@RequestBody Object settings) {
        String userId = UserContext.getCurrentUserId();
        Object updatedSettings = userService.updateSettings(userId, settings);
        return R.success(updatedSettings);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public R<Void> logout() {
        StpUtil.logout();
        return R.success();
    }
} 