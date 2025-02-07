package com.orjrs.admin.controller;

import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    @ApiOperation("获取角色列表")
    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                 @RequestParam(defaultValue = "10") Integer pageSize,
                 @RequestParam(required = false) String name) {
        return R.ok();
    }

    @ApiOperation("获取所有角色")
    @GetMapping("/all")
    public R listAll() {
        return R.ok();
    }

    @ApiOperation("获取角色详情")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("创建角色")
    @PostMapping
    public R create(@RequestBody RoleCreateDTO role) {
        return R.ok();
    }

    @ApiOperation("更新角色")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody RoleUpdateDTO role) {
        return R.ok();
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("分配权限")
    @PostMapping("/{id}/permissions")
    public R assignPermissions(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        return R.ok();
    }

    @ApiOperation("获取角色权限")
    @GetMapping("/{id}/permissions")
    public R getRolePermissions(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("获取角色用户列表")
    @GetMapping("/{id}/users")
    public R getRoleUsers(@PathVariable Long id,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok();
    }
} 