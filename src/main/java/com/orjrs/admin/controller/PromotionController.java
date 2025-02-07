package com.orjrs.admin.controller;

import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "促销活动接口")
@RestController
@RequestMapping("/marketing/promotion")
@RequiredArgsConstructor
public class PromotionController {

    @ApiOperation("获取促销活动列表")
    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                 @RequestParam(defaultValue = "10") Integer pageSize,
                 @RequestParam(required = false) String name,
                 @RequestParam(required = false) Integer status,
                 @RequestParam(required = false) Integer type) {
        return R.ok();
    }

    @ApiOperation("获取促销活动详情")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("创建促销活动")
    @PostMapping
    public R create(@RequestBody PromotionCreateDTO promotion) {
        return R.ok();
    }

    @ApiOperation("更新促销活动")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody PromotionUpdateDTO promotion) {
        return R.ok();
    }

    @ApiOperation("删除促销活动")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("开启促销活动")
    @PutMapping("/{id}/enable")
    public R enable(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("关闭促销活动")
    @PutMapping("/{id}/disable")
    public R disable(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("获取活动商品")
    @GetMapping("/{id}/foods")
    public R getPromotionFoods(@PathVariable Long id,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok();
    }

    @ApiOperation("添加活动商品")
    @PostMapping("/{id}/foods")
    public R addPromotionFoods(@PathVariable Long id, @RequestBody List<PromotionFoodDTO> foods) {
        return R.ok();
    }

    @ApiOperation("移除活动商品")
    @DeleteMapping("/{id}/foods")
    public R removePromotionFoods(@PathVariable Long id, @RequestBody List<Long> foodIds) {
        return R.ok();
    }

    @ApiOperation("获取活动统计")
    @GetMapping("/{id}/statistics")
    public R getStatistics(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("获取活动记录")
    @GetMapping("/{id}/records")
    public R getRecords(@PathVariable Long id,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok();
    }

    @ApiOperation("复制活动")
    @PostMapping("/{id}/copy")
    public R copyPromotion(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("获取活动参与用户")
    @GetMapping("/{id}/users")
    public R getPromotionUsers(@PathVariable Long id,
                            @RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok();
    }

    @ApiOperation("导出活动数据")
    @GetMapping("/export")
    public R exportPromotion() {
        return R.ok();
    }

    @ApiOperation("获取活动类型列表")
    @GetMapping("/types")
    public R getPromotionTypes() {
        return R.ok();
    }
} 