package com.orjrs.common.exception;

import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 错误消息
     */
    private final String message;
    private final int code;

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        this(message, 500);
    }

    public BusinessException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }
} 