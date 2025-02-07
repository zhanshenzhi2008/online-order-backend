package com.orjrs.admin.controller.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orjrs.admin.entity.goods.Goods;
import com.orjrs.admin.service.goods.GoodsService;
import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * 商品信息管理
 */
@Api(tags = "商品信息管理")
@RestController
@RequestMapping("/admin/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    @ApiOperation("分页查询商品列表")
    @GetMapping("/page")
    public R<IPage<Goods>> page(
            @ApiParam("页码") @RequestParam(defaultValue = "1") int page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") int size,
            @ApiParam("分类ID") @RequestParam(required = false) Long categoryId,
            @ApiParam("商品名称") @RequestParam(required = false) String name,
            @ApiParam("状态：0-下架，1-上架") @RequestParam(required = false) Integer status) {
        
        IPage<Goods> pageResult = goodsService.page(page, size, categoryId, name, status);
        return R.success(pageResult);
    }

    @ApiOperation("获取商品详情")
    @GetMapping("/{id}")
    public R<Goods> getDetail(@ApiParam("商品ID") @PathVariable Long id) {
        Goods goods = goodsService.getDetail(id);
        return R.success(goods);
    }

    @ApiOperation("新增商品")
    @PostMapping
    public R<Long> save(@ApiParam("商品信息") @Valid @RequestBody Goods goods) {
        goodsService.saveGoods(goods);
        return R.success(goods.getId());
    }

    @ApiOperation("修改商品")
    @PutMapping("/{id}")
    public R<Void> update(
            @ApiParam("商品ID") @PathVariable Long id,
            @ApiParam("商品信息") @Valid @RequestBody Goods goods) {
        goods.setId(id);
        goodsService.updateGoods(goods);
        return R.success();
    }

    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    public R<Void> delete(@ApiParam("商品ID") @PathVariable Long id) {
        goodsService.deleteGoods(id);
        return R.success();
    }

    @ApiOperation("批量更新状态")
    @PutMapping("/status/{status}")
    public R<Void> updateStatusBatch(
            @ApiParam("状态：0-下架，1-上架") @PathVariable Integer status,
            @ApiParam("商品ID列表") @RequestBody Long[] ids) {
        goodsService.updateStatusBatch(ids, status);
        return R.success();
    }

    @ApiOperation("更新商品库存")
    @PatchMapping("/{id}/stock")
    public R<Void> updateStock(
            @ApiParam("商品ID") @PathVariable Long id,
            @ApiParam("库存数量") @RequestParam Integer stock) {
        goodsService.updateStock(id, stock);
        return R.success();
    }

    @ApiOperation("更新商品价格")
    @PatchMapping("/{id}/price")
    public R<Void> updatePrice(
            @ApiParam("商品ID") @PathVariable Long id,
            @ApiParam("价格") @RequestParam BigDecimal price) {
        goodsService.updatePrice(id, price);
        return R.success();
    }

    @ApiOperation("导入商品")
    @PostMapping("/import")
    public R<Void> importGoods(@ApiParam("Excel文件") @RequestParam("file") MultipartFile file) {
        goodsService.importGoods(file);
        return R.success();
    }

    @ApiOperation("导出商品")
    @GetMapping("/export")
    public void exportGoods(
            @ApiParam("分类ID") @RequestParam(required = false) Long categoryId,
            @ApiParam("商品名称") @RequestParam(required = false) String name,
            @ApiParam("状态") @RequestParam(required = false) Integer status,
            HttpServletResponse response) {
        goodsService.exportGoods(categoryId, name, status, response);
    }
} 