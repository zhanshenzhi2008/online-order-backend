package com.orjrs.admin.controller.food;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.orjrs.admin.entity.food.FoodCategory;
import com.orjrs.admin.service.food.CategoryService;
import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 商品分类管理
 */
@Api(tags = "商品分类管理")
@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation("分页查询分类列表")
    @GetMapping("/page")
    public R<Page<FoodCategory>> page(
            @ApiParam("页码") @RequestParam(defaultValue = "1") int page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") int size,
            @ApiParam("分类名称") @RequestParam(required = false) String name,
            @ApiParam("状态：0-禁用，1-启用") @RequestParam(required = false) Integer status) {
        
        LambdaQueryWrapper<FoodCategory> wrapper = new LambdaQueryWrapper<FoodCategory>()
                .like(StringUtils.isNotBlank(name), FoodCategory::getName, name)
                .eq(status != null, FoodCategory::getStatus, status)
                .orderByAsc(FoodCategory::getSort);

        Page<FoodCategory> pageResult = categoryService.page(new Page<>(page, size), wrapper);
        return R.success(pageResult);
    }

    @ApiOperation("获取分类树形结构")
    @GetMapping("/tree")
    public R<List<FoodCategory>> tree() {
        List<FoodCategory> tree = categoryService.tree();
        return R.success(tree);
    }

    @ApiOperation("获取分类详情")
    @GetMapping("/{id}")
    public R<FoodCategory> getById(@ApiParam("分类ID") @PathVariable Long id) {
        FoodCategory category = categoryService.getById(id);
        return R.success(category);
    }

    @ApiOperation("新增分类")
    @PostMapping
    public R<Long> save(@ApiParam("分类信息") @Valid @RequestBody FoodCategory category) {
        categoryService.save(category);
        return R.success(category.getId());
    }

    @ApiOperation("修改分类")
    @PutMapping("/{id}")
    public R<Void> update(
            @ApiParam("分类ID") @PathVariable Long id,
            @ApiParam("分类信息") @Valid @RequestBody FoodCategory category) {
        category.setId(id);
        categoryService.updateById(category);
        return R.success();
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    public R<Void> delete(@ApiParam("分类ID") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return R.success();
    }

    @ApiOperation("批量更新状态")
    @PutMapping("/status/{status}")
    public R<Void> updateStatusBatch(
            @ApiParam("状态：0-禁用，1-启用") @PathVariable Integer status,
            @ApiParam("分类ID列表") @RequestBody Long[] ids) {
        categoryService.updateStatusBatch(ids, status);
        return R.success();
    }
} 