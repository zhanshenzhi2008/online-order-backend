package com.orjrs.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用返回结果
 */
@Data
@ApiModel("通用响应")
public class R<T> {
    @ApiModelProperty("响应码")
    private Integer code;

    @ApiModelProperty("响应消息")
    private String message;

    @ApiModelProperty("响应数据")
    private T data;

    private R() {
    }

    private R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> success() {
        return new R<>(200, "success", null);
    }

    public static <T> R<T> success(T data) {
        return new R<>(200, "success", data);
    }

    public static <T> R<T> success(String message, T data) {
        return new R<>(200, message, data);
    }

    public static <T> R<T> error() {
        return new R<>(500, "error", null);
    }

    public static <T> R<T> error(String message) {
        return new R<>(500, message, null);
    }

    public static <T> R<T> error(Integer code, String message) {
        return new R<>(code, message, null);
    }
} 