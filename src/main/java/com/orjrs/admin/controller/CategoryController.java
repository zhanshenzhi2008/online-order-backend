package com.orjrs.admin.controller;

import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "商品分类接口")
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    @ApiOperation("获取分类树")
    @GetMapping("/tree")
    public R getCategoryTree() {
        return R.ok();
    }

    @ApiOperation("获取分类列表")
    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                 @RequestParam(defaultValue = "10") Integer pageSize,
                 @RequestParam(required = false) String name) {
        return R.ok();
    }

    @ApiOperation("获取分类详情")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("创建分类")
    @PostMapping
    public R create(@RequestBody CategoryCreateDTO category) {
        return R.ok();
    }

    @ApiOperation("更新分类")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody CategoryUpdateDTO category) {
        return R.ok();
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("更新分类状态")
    @PutMapping("/{id}/status")
    public R updateStatus(@PathVariable Long id, @RequestParam Boolean status) {
        return R.ok();
    }

    @ApiOperation("更新分类排序")
    @PutMapping("/{id}/sort")
    public R updateSort(@PathVariable Long id, @RequestParam Integer sort) {
        return R.ok();
    }

    @ApiOperation("批量删除分类")
    @DeleteMapping("/batch")
    public R batchDelete(@RequestBody List<Long> ids) {
        return R.ok();
    }

    @ApiOperation("获取子分类")
    @GetMapping("/{id}/children")
    public R getChildren(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("获取分类路径")
    @GetMapping("/{id}/path")
    public R getCategoryPath(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("检查分类名称是否存在")
    @GetMapping("/check")
    public R checkName(@RequestParam String name, @RequestParam(required = false) Long parentId) {
        return R.ok();
    }
} 