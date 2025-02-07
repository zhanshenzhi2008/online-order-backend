package com.orjrs.admin.controller.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orjrs.admin.entity.goods.Category;
import com.orjrs.admin.service.goods.CategoryService;
import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "商品分类管理")
@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation("分页查询分类列表")
    @GetMapping("/page")
    public R<IPage<Category>> page(
            @ApiParam("页码") @RequestParam(defaultValue = "1") int page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") int size,
            @ApiParam("分类名称") @RequestParam(required = false) String name,
            @ApiParam("状态：0-禁用，1-启用") @RequestParam(required = false) Integer status) {
        
        IPage<Category> pageResult = categoryService.page(page, size, name, status);
        return R.success(pageResult);
    }

    @ApiOperation("获取分类树形结构")
    @GetMapping("/tree")
    public R<List<Category>> tree() {
        List<Category> tree = categoryService.tree();
        return R.success(tree);
    }

    @ApiOperation("创建分类")
    @PostMapping
    public R<Void> create(@ApiParam("分类信息") @Valid @RequestBody Category category) {
        categoryService.createCategory(category);
        return R.success();
    }

    @ApiOperation("更新分类")
    @PutMapping("/{id}")
    public R<Void> update(
            @ApiParam("分类ID") @PathVariable Long id,
            @ApiParam("分类信息") @Valid @RequestBody Category category) {
        category.setId(id);
        categoryService.updateCategory(category);
        return R.success();
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    public R<Void> delete(@ApiParam("分类ID") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return R.success();
    }

    @ApiOperation("更新分类状态")
    @PatchMapping("/{id}/status/{status}")
    public R<Void> updateStatus(
            @ApiParam("分类ID") @PathVariable Long id,
            @ApiParam("状态：0-禁用，1-启用") @PathVariable Integer status) {
        categoryService.updateStatus(id, status);
        return R.success();
    }
} 