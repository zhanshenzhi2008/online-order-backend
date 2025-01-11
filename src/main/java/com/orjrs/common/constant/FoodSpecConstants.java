package com.orjrs.common.constant;

import com.orjrs.common.enums.AdditionType;
import com.orjrs.common.enums.SpecType;
import com.orjrs.utils.FoodSpecUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 食品规格常量类
 */
public class FoodSpecConstants {
    /**
     * 默认规格选项
     */
    public static final List<Map<String, Object>> DEFAULT_SPECS = Arrays.asList(
        // 规格大小
        FoodSpecUtils.createSpecOption(SpecType.SIZE, "大杯", new BigDecimal("2.00")),
        FoodSpecUtils.createSpecOption(SpecType.SIZE, "中杯", BigDecimal.ZERO),
        FoodSpecUtils.createSpecOption(SpecType.SIZE, "小杯", new BigDecimal("-2.00")),
        
        // 温度
        FoodSpecUtils.createSpecOption(SpecType.TEMPERATURE, "热", BigDecimal.ZERO),
        FoodSpecUtils.createSpecOption(SpecType.TEMPERATURE, "温", BigDecimal.ZERO),
        FoodSpecUtils.createSpecOption(SpecType.TEMPERATURE, "常温", BigDecimal.ZERO),
        FoodSpecUtils.createSpecOption(SpecType.TEMPERATURE, "去冰", BigDecimal.ZERO),
        FoodSpecUtils.createSpecOption(SpecType.TEMPERATURE, "少冰", BigDecimal.ZERO),
        FoodSpecUtils.createSpecOption(SpecType.TEMPERATURE, "多冰", BigDecimal.ZERO),
        
        // 甜度
        FoodSpecUtils.createSpecOption(SpecType.SWEETNESS, "全糖", BigDecimal.ZERO),
        FoodSpecUtils.createSpecOption(SpecType.SWEETNESS, "七分糖", BigDecimal.ZERO),
        FoodSpecUtils.createSpecOption(SpecType.SWEETNESS, "五分糖", BigDecimal.ZERO),
        FoodSpecUtils.createSpecOption(SpecType.SWEETNESS, "三分糖", BigDecimal.ZERO),
        FoodSpecUtils.createSpecOption(SpecType.SWEETNESS, "无糖", BigDecimal.ZERO)
    );
    
    /**
     * 默认加料选项
     */
    public static final List<Map<String, Object>> DEFAULT_ADDITIONS = Arrays.asList(
        FoodSpecUtils.createAdditionOption(AdditionType.PEARL, new BigDecimal("2.00")),
        FoodSpecUtils.createAdditionOption(AdditionType.COCONUT, new BigDecimal("2.00")),
        FoodSpecUtils.createAdditionOption(AdditionType.PUDDING, new BigDecimal("3.00")),
        FoodSpecUtils.createAdditionOption(AdditionType.GRASS_JELLY, new BigDecimal("2.00")),
        FoodSpecUtils.createAdditionOption(AdditionType.RED_BEAN, new BigDecimal("2.00")),
        FoodSpecUtils.createAdditionOption(AdditionType.ALOE, new BigDecimal("2.00"))
    );
    
    private FoodSpecConstants() {
        // 私有构造函数，防止实例化
    }
} 