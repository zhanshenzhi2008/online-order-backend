package com.orjrs.miniapp.controller;

import com.orjrs.common.R;
import com.orjrs.miniapp.entity.Food;
import com.orjrs.miniapp.service.FoodService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "食品管理", description = "食品相关接口")
@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @Operation(summary = "获取食品列表")
    @GetMapping("/list")
    public R<List<Food>> getFoodList(
            @Parameter(description = "分类ID") @RequestParam(required = false) String categoryId) {
        List<Food> foodList = foodService.getFoodsByCategory(categoryId);
        return R.success(foodList);
    }

    @Operation(summary = "获取食品详情")
    @GetMapping("/detail")
    public R<Food> getFoodDetail(
            @Parameter(description = "食品ID") @RequestParam String id) {
        Food food = foodService.getFoodDetail(id);
        return food != null ? R.success(food) : R.error("食品不存在");
    }

    @Operation(summary = "获取推荐食品")
    @GetMapping("/recommend")
    public R<List<Food>> getRecommendFoods() {
        List<Food> foodList = foodService.getRecommendFoods();
        return R.success(foodList);
    }

    @Operation(summary = "搜索食品")
    @GetMapping("/search")
    public R<List<Food>> searchFoods(
            @Parameter(description = "搜索关键词") @RequestParam String keyword) {
        List<Food> foodList = foodService.searchFoods(keyword);
        return R.success(foodList);
    }
} 