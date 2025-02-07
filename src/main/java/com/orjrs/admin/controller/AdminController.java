package com.orjrs.admin.controller;

import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理员管理接口")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @ApiOperation("获取管理员列表")
    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                 @RequestParam(defaultValue = "10") Integer pageSize,
                 @RequestParam(required = false) String username) {
        return R.ok();
    }

    @ApiOperation("获取管理员详情")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("创建管理员")
    @PostMapping
    public R create(@RequestBody AdminCreateDTO admin) {
        return R.ok();
    }

    @ApiOperation("更新管理员")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody AdminUpdateDTO admin) {
        return R.ok();
    }

    @ApiOperation("删除管理员")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("分配角色")
    @PostMapping("/{id}/roles")
    public R assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        return R.ok();
    }

    @ApiOperation("重置密码")
    @PutMapping("/{id}/password/reset")
    public R resetPassword(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("修改密码")
    @PutMapping("/password")
    public R updatePassword(@RequestBody PasswordUpdateDTO passwordUpdate) {
        return R.ok();
    }

    @ApiOperation("获取当前管理员信息")
    @GetMapping("/info")
    public R getCurrentAdminInfo() {
        return R.ok();
    }

    @ApiOperation("更新当前管理员信息")
    @PutMapping("/info")
    public R updateCurrentAdminInfo(@RequestBody AdminInfoUpdateDTO adminInfo) {
        return R.ok();
    }
} 