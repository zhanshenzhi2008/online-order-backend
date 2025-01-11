package com.orjrs.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "规格类型枚举")
public enum SpecType {
    SIZE("size", "规格大小"),
    TEMPERATURE("temperature", "温度"),
    SWEETNESS("sweetness", "甜度"),
    ICE("ice", "冰量");

    private final String code;
    private final String description;

    SpecType(String code, String description) {
        this.code = code;
        this.description = description;
    }
} 