package com.orjrs.admin.controller;

import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "满减活动接口")
@RestController
@RequestMapping("/marketing/full-reduction")
@RequiredArgsConstructor
public class FullReductionController {

    @ApiOperation("获取满减活动列表")
    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                 @RequestParam(defaultValue = "10") Integer pageSize,
                 @RequestParam(required = false) String name,
                 @RequestParam(required = false) Integer status) {
        return R.ok();
    }

    @ApiOperation("获取满减活动详情")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("创建满减活动")
    @PostMapping
    public R create(@RequestBody FullReductionCreateDTO fullReduction) {
        return R.ok();
    }

    @ApiOperation("更新满减活动")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody FullReductionUpdateDTO fullReduction) {
        return R.ok();
    }

    @ApiOperation("删除满减活动")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("开启满减活动")
    @PutMapping("/{id}/enable")
    public R enable(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("关闭满减活动")
    @PutMapping("/{id}/disable")
    public R disable(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("获取活动商品")
    @GetMapping("/{id}/foods")
    public R getActivityFoods(@PathVariable Long id,
                            @RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok();
    }

    @ApiOperation("添加活动商品")
    @PostMapping("/{id}/foods")
    public R addActivityFoods(@PathVariable Long id, @RequestBody List<Long> foodIds) {
        return R.ok();
    }

    @ApiOperation("移除活动商品")
    @DeleteMapping("/{id}/foods")
    public R removeActivityFoods(@PathVariable Long id, @RequestBody List<Long> foodIds) {
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
    public R copyActivity(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("检查活动名称是否存在")
    @GetMapping("/check")
    public R checkName(@RequestParam String name) {
        return R.ok();
    }
} 