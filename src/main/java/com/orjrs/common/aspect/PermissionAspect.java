package com.orjrs.common.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.orjrs.common.annotation.RequirePermission;
import com.orjrs.common.enums.UserPermission;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 权限校验切面
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Before("@annotation(com.orjrs.common.annotation.RequirePermission)")
    public void checkPermission(JoinPoint point) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 获取权限注解
        RequirePermission requirePermission = method.getAnnotation(RequirePermission.class);
        if (requirePermission == null) {
            return;
        }

        // 获取需要的权限
        UserPermission[] permissions = requirePermission.value();
        boolean requireAll = requirePermission.all();

        // 检查权限
        if (requireAll) {
            // 需要全部权限
            Arrays.stream(permissions).forEach(permission -> 
                StpUtil.checkPermission(permission.getCode())
            );
        } else {
            // 任一权限即可
            boolean hasPermission = Arrays.stream(permissions)
                    .anyMatch(permission -> StpUtil.hasPermission(permission.getCode()));
            if (!hasPermission) {
                throw new RuntimeException("无权限访问");
            }
        }
    }
} 