package com.orjrs.admin.controller;

import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "价格管理接口")
@RestController
@RequestMapping("/price")
@RequiredArgsConstructor
public class PriceController {

    @ApiOperation("获取价格列表")
    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                 @RequestParam(defaultValue = "10") Integer pageSize,
                 @RequestParam(required = false) String foodName) {
        return R.ok();
    }

    @ApiOperation("获取价格详情")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("更新价格")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody PriceUpdateDTO price) {
        return R.ok();
    }

    @ApiOperation("批量更新价格")
    @PutMapping("/batch")
    public R batchUpdate(@RequestBody List<PriceUpdateDTO> prices) {
        return R.ok();
    }

    @ApiOperation("设置折扣")
    @PostMapping("/{id}/discount")
    public R setDiscount(@PathVariable Long id, @RequestBody DiscountDTO discount) {
        return R.ok();
    }

    @ApiOperation("批量设置折扣")
    @PostMapping("/discount/batch")
    public R batchSetDiscount(@RequestBody BatchDiscountDTO batchDiscount) {
        return R.ok();
    }

    @ApiOperation("获取价格历史")
    @GetMapping("/{id}/history")
    public R getPriceHistory(@PathVariable Long id,
                           @RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok();
    }

    @ApiOperation("设置特价")
    @PostMapping("/{id}/special")
    public R setSpecialPrice(@PathVariable Long id, @RequestBody SpecialPriceDTO specialPrice) {
        return R.ok();
    }

    @ApiOperation("取消特价")
    @DeleteMapping("/{id}/special")
    public R cancelSpecialPrice(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("获取特价商品列表")
    @GetMapping("/special/list")
    public R getSpecialPriceList(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok();
    }

    @ApiOperation("设置会员价")
    @PostMapping("/{id}/member")
    public R setMemberPrice(@PathVariable Long id, @RequestBody List<MemberPriceDTO> memberPrices) {
        return R.ok();
    }

    @ApiOperation("获取会员价列表")
    @GetMapping("/{id}/member")
    public R getMemberPriceList(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("导出价格数据")
    @GetMapping("/export")
    public R exportPrice() {
        return R.ok();
    }

    @ApiOperation("导入价格数据")
    @PostMapping("/import")
    public R importPrice(@RequestBody MultipartFile file) {
        return R.ok();
    }
} 