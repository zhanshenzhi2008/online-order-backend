package com.orjrs.admin.controller.food;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orjrs.admin.entity.food.Food;
import com.orjrs.admin.service.food.FoodService;
import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * 商品信息管理
 */
@Api(tags = "商品信息管理")
@RestController
@RequestMapping("/admin/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @ApiOperation("分页查询商品列表")
    @GetMapping("/page")
    public R<IPage<Food>> page(
            @ApiParam("页码") @RequestParam(defaultValue = "1") int page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") int size,
            @ApiParam("分类ID") @RequestParam(required = false) Long categoryId,
            @ApiParam("商品名称") @RequestParam(required = false) String name,
            @ApiParam("状态：0-下架，1-上架") @RequestParam(required = false) Integer status) {
        
        IPage<Food> pageResult = foodService.page(page, size, categoryId, name, status);
        return R.success(pageResult);
    }

    @ApiOperation("获取商品详情")
    @GetMapping("/{id}")
    public R<Food> getDetail(@ApiParam("商品ID") @PathVariable Long id) {
        Food food = foodService.getDetail(id);
        return R.success(food);
    }

    @ApiOperation("新增商品")
    @PostMapping
    public R<Long> save(@ApiParam("商品信息") @Valid @RequestBody Food food) {
        foodService.saveFood(food);
        return R.success(food.getId());
    }

    @ApiOperation("修改商品")
    @PutMapping("/{id}")
    public R<Void> update(
            @ApiParam("商品ID") @PathVariable Long id,
            @ApiParam("商品信息") @Valid @RequestBody Food food) {
        food.setId(id);
        foodService.updateFood(food);
        return R.success();
    }

    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    public R<Void> delete(@ApiParam("商品ID") @PathVariable Long id) {
        foodService.deleteFood(id);
        return R.success();
    }

    @ApiOperation("批量更新状态")
    @PutMapping("/status/{status}")
    public R<Void> updateStatusBatch(
            @ApiParam("状态：0-下架，1-上架") @PathVariable Integer status,
            @ApiParam("商品ID列表") @RequestBody Long[] ids) {
        foodService.updateStatusBatch(ids, status);
        return R.success();
    }

    @ApiOperation("更新销量")
    @PutMapping("/{id}/sales/{count}")
    public R<Void> updateSales(
            @ApiParam("商品ID") @PathVariable Long id,
            @ApiParam("增加的数量，可以为负数") @PathVariable Integer count) {
        foodService.updateSales(id, count);
        return R.success();
    }

    @ApiOperation("更新评分")
    @PutMapping("/{id}/rating/{rating}")
    public R<Void> updateRating(
            @ApiParam("商品ID") @PathVariable Long id,
            @ApiParam("评分") @PathVariable BigDecimal rating) {
        foodService.updateRating(id, rating);
        return R.success();
    }
} 