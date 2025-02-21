package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.domain.R;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDept;
import com.sse.sseapp.domain.system.SysUser;
import com.sse.sseapp.domain.system.model.LoginUser;
import com.sse.sseapp.security.annotation.InnerAuth;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统管理-用户管理-feign
 *
 * @author zhengyaosheng
 * @data 2023-02-25
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/user")
public interface ISysUserFeign {

    /**
     * 列表获取
     *
     * @param user
     * @return
     */
    @PostMapping("/list")
    AjaxResult list(@RequestBody SysUser user);

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{username}")
    R<LoginUser> info(@PathVariable(value = "username") String username);

    /**
     * 注册用户信息
     */
    @InnerAuth
    @PostMapping("/register")
    R<Boolean> register(@RequestBody SysUser sysUser);

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    AjaxResult getInfo(@RequestParam(value = "userId") Long userId);

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = {"/query/{userId}"})
    AjaxResult query(@PathVariable(value = "userId", required = false) Long userId);

    /**
     * 新增用户
     */
    @PostMapping("/add")
    AjaxResult add(@Validated @RequestBody SysUser user);

    /**
     * 修改用户
     */
    @PutMapping("update")
    AjaxResult edit(@Validated @RequestBody SysUser user);

    /**
     * 删除用户
     */
    @DeleteMapping("/remove")
    AjaxResult remove(@RequestParam("userIds") Long[] userIds, @RequestParam("userId") Long loginUserId);

    /**
     * 重置密码
     */
    @PutMapping("/resetPwd")
    AjaxResult resetPwd(@RequestBody SysUser user);

    /**
     * 状态修改
     */
    @PutMapping("/changeStatus")
    AjaxResult changeStatus(@RequestBody SysUser user);

    /**
     * 根据用户编号获取授权角色
     */
    @GetMapping("/authRole/{userId}")
    AjaxResult authRole(@PathVariable("userId") Long userId);

    /**
     * 用户授权角色
     */
    @PutMapping("/authRole")
    AjaxResult insertAuthRole(@RequestParam("userId") Long userId, @RequestParam("roleIds") Long[] roleIds);

    /**
     * 获取部门树列表
     */
    @GetMapping("/deptTree")
    AjaxResult deptTree(SysDept dept);

}
