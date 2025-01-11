package com.orjrs.utils;

import com.orjrs.common.enums.AdditionType;
import com.orjrs.common.enums.SpecType;
import com.orjrs.common.exception.BusinessException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 食品规格工具类
 */
public class FoodSpecUtils {
    
    /**
     * 创建规格选项
     *
     * @param type 规格类型
     * @param name 选项名称
     * @param price 价格调整
     * @return 规格选项
     */
    public static Map<String, Object> createSpecOption(SpecType type, String name, BigDecimal price) {
        Map<String, Object> option = new HashMap<>();
        option.put("type", type.getCode());
        option.put("name", name);
        option.put("price", price);
        return option;
    }
    
    /**
     * 创建加料选项
     *
     * @param type 加料类型
     * @param price 价格
     * @return 加料选项
     */
    public static Map<String, Object> createAdditionOption(AdditionType type, BigDecimal price) {
        Map<String, Object> option = new HashMap<>();
        option.put("type", type.getCode());
        option.put("name", type.getDescription());
        option.put("price", price);
        return option;
    }
    
    /**
     * 验证规格信息
     *
     * @param specs 规格信息
     * @param availableSpecs 可用规格选项
     * @throws BusinessException 如果规格信息无效
     */
    public static void validateSpecs(Map<String, Object> specs, List<Map<String, Object>> availableSpecs) {
        if (specs == null || availableSpecs == null) {
            return;
        }
        
        for (Map.Entry<String, Object> entry : specs.entrySet()) {
            String type = entry.getKey();
            String value = entry.getValue().toString();
            boolean valid = false;
            
            for (Map<String, Object> spec : availableSpecs) {
                if (type.equals(spec.get("type")) && value.equals(spec.get("name"))) {
                    valid = true;
                    break;
                }
            }
            
            if (!valid) {
                throw new BusinessException("无效的规格选择：" + type + " - " + value);
            }
        }
    }
    
    /**
     * 验证加料信息
     *
     * @param additions 加料信息
     * @param availableAdditions 可用加料选项
     * @throws BusinessException 如果加料信息无效
     */
    public static void validateAdditions(Map<String, Object> additions, List<Map<String, Object>> availableAdditions) {
        if (additions == null || availableAdditions == null) {
            return;
        }
        
        for (Map.Entry<String, Object> entry : additions.entrySet()) {
            String type = entry.getKey();
            boolean valid = false;
            
            for (Map<String, Object> addition : availableAdditions) {
                if (type.equals(addition.get("type"))) {
                    valid = true;
                    break;
                }
            }
            
            if (!valid) {
                throw new BusinessException("无效的加料选择：" + type);
            }
        }
    }
    
    /**
     * 计算规格和加料的额外费用
     *
     * @param specs 规格信息
     * @param additions 加料信息
     * @param availableSpecs 可用规格选项
     * @param availableAdditions 可用加料选项
     * @return 额外费用
     */
    public static BigDecimal calculateExtraFee(
            Map<String, Object> specs,
            Map<String, Object> additions,
            List<Map<String, Object>> availableSpecs,
            List<Map<String, Object>> availableAdditions) {
        BigDecimal extraFee = BigDecimal.ZERO;
        
        // 计算规格费用
        if (specs != null && availableSpecs != null) {
            for (Map.Entry<String, Object> entry : specs.entrySet()) {
                String type = entry.getKey();
                String value = entry.getValue().toString();
                
                for (Map<String, Object> spec : availableSpecs) {
                    if (type.equals(spec.get("type")) && value.equals(spec.get("name"))) {
                        BigDecimal price = new BigDecimal(spec.get("price").toString());
                        extraFee = extraFee.add(price);
                        break;
                    }
                }
            }
        }
        
        // 计算加料费用
        if (additions != null && availableAdditions != null) {
            for (Map.Entry<String, Object> entry : additions.entrySet()) {
                String type = entry.getKey();
                
                for (Map<String, Object> addition : availableAdditions) {
                    if (type.equals(addition.get("type"))) {
                        BigDecimal price = new BigDecimal(addition.get("price").toString());
                        extraFee = extraFee.add(price);
                        break;
                    }
                }
            }
        }
        
        return extraFee;
    }
} 