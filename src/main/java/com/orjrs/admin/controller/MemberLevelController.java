package com.orjrs.admin.controller;

import com.orjrs.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "会员等级接口")
@RestController
@RequestMapping("/member/level")
@RequiredArgsConstructor
public class MemberLevelController {

    @ApiOperation("获取会员等级列表")
    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                 @RequestParam(defaultValue = "10") Integer pageSize,
                 @RequestParam(required = false) String name) {
        return R.ok();
    }

    @ApiOperation("获取所有会员等级")
    @GetMapping("/all")
    public R listAll() {
        return R.ok();
    }

    @ApiOperation("获取会员等级详情")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("创建会员等级")
    @PostMapping
    public R create(@RequestBody MemberLevelCreateDTO level) {
        return R.ok();
    }

    @ApiOperation("更新会员等级")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody MemberLevelUpdateDTO level) {
        return R.ok();
    }

    @ApiOperation("删除会员等级")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("设置等级权益")
    @PostMapping("/{id}/privileges")
    public R setPrivileges(@PathVariable Long id, @RequestBody List<PrivilegeDTO> privileges) {
        return R.ok();
    }

    @ApiOperation("获取等级权益")
    @GetMapping("/{id}/privileges")
    public R getPrivileges(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("设置升级规则")
    @PostMapping("/{id}/upgrade-rules")
    public R setUpgradeRules(@PathVariable Long id, @RequestBody List<UpgradeRuleDTO> rules) {
        return R.ok();
    }

    @ApiOperation("获取升级规则")
    @GetMapping("/{id}/upgrade-rules")
    public R getUpgradeRules(@PathVariable Long id) {
        return R.ok();
    }

    @ApiOperation("获取等级会员列表")
    @GetMapping("/{id}/members")
    public R getLevelMembers(@PathVariable Long id,
                          @RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok();
    }

    @ApiOperation("更新等级状态")
    @PutMapping("/{id}/status")
    public R updateStatus(@PathVariable Long id, @RequestParam Boolean status) {
        return R.ok();
    }

    @ApiOperation("更新等级排序")
    @PutMapping("/{id}/sort")
    public R updateSort(@PathVariable Long id, @RequestParam Integer sort) {
        return R.ok();
    }

    @ApiOperation("检查等级名称是否存在")
    @GetMapping("/check")
    public R checkName(@RequestParam String name) {
        return R.ok();
    }

    @ApiOperation("获取等级统计信息")
    @GetMapping("/{id}/statistics")
    public R getLevelStatistics(@PathVariable Long id) {
        return R.ok();
    }
} 