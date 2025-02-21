package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.core.web.page.TableDataInfo;
import com.sse.sseapp.domain.system.SysRole;
import com.sse.sseapp.domain.system.SysUser;
import com.sse.sseapp.domain.system.SysUserRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统管理-角色管理-feign
 *
 * @author zhengyaosheng
 * @data 2023-02-25
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/role")
public interface ISysRoleFeign {

    /**
     * 列表获取
     *
     * @param role
     * @return
     */
    @PostMapping("/list")
    AjaxResult list(SysRole role);

    /**
     * 根据角色编号获取详细信息
     */
    @GetMapping(value = "/query/{roleId}")
    AjaxResult getInfo(@PathVariable(value = "roleId") Long roleId);

    /**
     * 新增角色
     */
    @PostMapping("/add")
    AjaxResult add(@Validated @RequestBody SysRole role);

    /**
     * 修改保存角色
     */
    @PutMapping("/edit")
    AjaxResult edit(@Validated @RequestBody SysRole role);

    /**
     * 修改保存数据权限
     */
    @PutMapping("/dataScope")
    AjaxResult dataScope(@RequestBody SysRole role);

    /**
     * 状态修改
     */
    @PutMapping("/changeStatus")
    AjaxResult changeStatus(@RequestBody SysRole role);

    /**
     * 删除角色
     */
    @DeleteMapping("/remove/{roleIds}")
    AjaxResult remove(@PathVariable(value = "roleIds") Long[] roleIds);

    /**
     * 获取角色选择框列表
     */
    @GetMapping("/optionSelect")
    AjaxResult optionSelect();

    /**
     * 查询已分配用户角色列表
     */
    @GetMapping("/authUser/allocatedList")
    AjaxResult allocatedList(SysUser user);

    /**
     * 查询未分配用户角色列表
     */
    @GetMapping("/authUser/unallocatedList")
    AjaxResult unallocatedList(SysUser user);

    /**
     * 取消授权用户
     */
    @PutMapping("/authUser/cancel")
    AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole);

    /**
     * 批量取消授权用户
     */
    @PutMapping("/authUser/cancelAll")
    AjaxResult cancelAuthUserAll(@RequestParam("roleId") Long roleId, @RequestParam("userIds") Long[] userIds);

    /**
     * 批量选择用户授权
     */
    @PutMapping("/authUser/selectAll")
    AjaxResult selectAuthUserAll(@RequestParam("roleId") Long roleId, @RequestParam("userIds") Long[] userIds);

    /**
     * 获取对应角色部门树列表
     */
    @GetMapping(value = "/deptTree/{roleId}")
    AjaxResult deptTree(@PathVariable("roleId") Long roleId);
}
