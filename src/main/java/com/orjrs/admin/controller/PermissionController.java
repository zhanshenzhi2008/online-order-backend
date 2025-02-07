package com.orjrs.admin.controller;

import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "权限管理接口")
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    @ApiOperation("获取权限树")
    @GetMapping("/tree")
    public R getPermissionTree() {
        return R.ok();
    }

    @ApiOperation("获取所有权限列表")
    @GetMapping("/list")
    public R list() {
        return R.ok();
    }

    @ApiOperation("获取权限详情")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("创建权限")
    @PostMapping
    public R create(@RequestBody PermissionCreateDTO permission) {
        return R.ok();
    }

    @ApiOperation("更新权限")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody PermissionUpdateDTO permission) {
        return R.ok();
    }

    @ApiOperation("删除权限")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("获取当前用户权限")
    @GetMapping("/current")
    public R getCurrentUserPermissions() {
        return R.ok();
    }

    @ApiOperation("获取指定用户的权限")
    @GetMapping("/user/{userId}")
    public R getUserPermissions(@PathVariable Long userId) {
        return R.ok();
    }

    @ApiOperation("获取指定角色的权限")
    @GetMapping("/role/{roleId}")
    public R getRolePermissions(@PathVariable Long roleId) {
        return R.ok();
    }

    @ApiOperation("批量删除权限")
    @DeleteMapping("/batch")
    public R batchDelete(@RequestBody List<Long> ids) {
        return R.ok();
    }

    @ApiOperation("更新权限状态")
    @PutMapping("/{id}/status")
    public R updateStatus(@PathVariable Long id, @RequestParam Boolean status) {
        return R.ok();
    }
} 