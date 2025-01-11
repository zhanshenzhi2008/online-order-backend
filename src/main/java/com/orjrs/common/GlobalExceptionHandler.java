package com.orjrs.common;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BaseException.class)
    public R<Object> handleBaseException(BaseException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理Sa-Token未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public R<Object> handleNotLoginException(NotLoginException e) {
        log.error("未登录：{}", e.getMessage());
        return R.error(401, "请先登录");
    }

    /**
     * 处理Sa-Token无权限异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public R<Object> handleNotPermissionException(NotPermissionException e) {
        log.error("无权限：{}", e.getMessage());
        return R.error(403, "无权限访问");
    }

    /**
     * 处理Sa-Token无角色异常
     */
    @ExceptionHandler(NotRoleException.class)
    public R<Object> handleNotRoleException(NotRoleException e) {
        log.error("无角色权限：{}", e.getMessage());
        return R.error(403, "无角色权限");
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Object> handleValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String message = sb.substring(0, sb.length() - 2);
        return R.error(message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public R<Object> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String message = sb.substring(0, sb.length() - 2);
        return R.error(message);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public R<Object> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error("系统异常，请联系管理员");
    }
} 