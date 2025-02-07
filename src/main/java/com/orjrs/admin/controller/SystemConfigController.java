package com.orjrs.admin.controller;

import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "系统配置接口")
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class SystemConfigController {

    @ApiOperation("获取系统配置列表")
    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                 @RequestParam(defaultValue = "10") Integer pageSize,
                 @RequestParam(required = false) String key) {
        return R.ok();
    }

    @ApiOperation("获取系统配置详情")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("根据键获取配置")
    @GetMapping("/key/{key}")
    public R getByKey(@PathVariable String key) {
        return R.ok();
    }

    @ApiOperation("创建系统配置")
    @PostMapping
    public R create(@RequestBody SystemConfigCreateDTO config) {
        return R.ok();
    }

    @ApiOperation("更新系统配置")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody SystemConfigUpdateDTO config) {
        return R.ok();
    }

    @ApiOperation("删除系统配置")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("批量更新系统配置")
    @PutMapping("/batch")
    public R batchUpdate(@RequestBody List<SystemConfigUpdateDTO> configs) {
        return R.ok();
    }

    @ApiOperation("刷新系统配置缓存")
    @PostMapping("/refresh")
    public R refreshCache() {
        return R.ok();
    }

    @ApiOperation("获取系统基础配置")
    @GetMapping("/basic")
    public R getBasicConfig() {
        return R.ok();
    }

    @ApiOperation("更新系统基础配置")
    @PutMapping("/basic")
    public R updateBasicConfig(@RequestBody SystemBasicConfigDTO config) {
        return R.ok();
    }

    @ApiOperation("获取系统邮件配置")
    @GetMapping("/email")
    public R getEmailConfig() {
        return R.ok();
    }

    @ApiOperation("更新系统邮件配置")
    @PutMapping("/email")
    public R updateEmailConfig(@RequestBody SystemEmailConfigDTO config) {
        return R.ok();
    }

    @ApiOperation("获取系统短信配置")
    @GetMapping("/sms")
    public R getSmsConfig() {
        return R.ok();
    }

    @ApiOperation("更新系统短信配置")
    @PutMapping("/sms")
    public R updateSmsConfig(@RequestBody SystemSmsConfigDTO config) {
        return R.ok();
    }

    @ApiOperation("获取系统支付配置")
    @GetMapping("/payment")
    public R getPaymentConfig() {
        return R.ok();
    }

    @ApiOperation("更新系统支付配置")
    @PutMapping("/payment")
    public R updatePaymentConfig(@RequestBody SystemPaymentConfigDTO config) {
        return R.ok();
    }
} 