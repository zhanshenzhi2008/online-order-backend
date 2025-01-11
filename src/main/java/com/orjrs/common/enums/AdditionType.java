package com.orjrs.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "加料类型枚举")
public enum AdditionType {
    PEARL("pearl", "珍珠"),
    COCONUT("coconut", "椰果"),
    PUDDING("pudding", "布丁"),
    GRASS_JELLY("grass_jelly", "仙草"),
    RED_BEAN("red_bean", "红豆"),
    ALOE("aloe", "芦荟");

    private final String code;
    private final String description;

    AdditionType(String code, String description) {
        this.code = code;
        this.description = description;
    }
} 