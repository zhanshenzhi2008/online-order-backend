package com.orjrs.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通用返回结果
 */
@Data
@Schema(description = "通用返回结果")
public class R<T> {
    @Schema(description = "响应码")
    private Integer code;

    @Schema(description = "响应消息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    private R() {
    }

    private R(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> ok() {
        return new R<>(0, "success");
    }

    public static <T> R<T> ok(T data) {
        return new R<>(0, "success", data);
    }

    public static <T> R<T> ok(String message, T data) {
        return new R<>(0, message, data);
    }

    public static <T> R<T> error(String message) {
        return new R<>(1, message);
    }

    public static <T> R<T> error(Integer code, String message) {
        return new R<>(code, message);
    }
} 