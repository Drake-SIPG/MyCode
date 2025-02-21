package com.sse.sseapp.controller;

import com.sse.sseapp.core.annotation.Log;
import com.sse.sseapp.core.enums.BusinessType;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysRole;
import com.sse.sseapp.domain.system.SysUser;
import com.sse.sseapp.domain.system.SysUserRole;
import com.sse.sseapp.security.annotation.RequiresPermissions;
import com.sse.sseapp.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 角色信息
 *
 * @author zhengyaosheng
 * @date 2021-02-23
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    @Autowired
    private SysRoleService roleService;

    /**
     * 列表查询
     *
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysRole role) {
        return roleService.list(role);
    }

    /**
     * 根据角色编号获取详细信息
     */
    @RequiresPermissions("system:role:query")
    @GetMapping(value = "/query/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        return roleService.getInfo(roleId);
    }

    /**
     * 新增角色
     */
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        return roleService.add(role);
    }

    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        return roleService.edit(role);
    }

    /**
     * 修改保存数据权限
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        return roleService.dataScope(role);
    }

    /**
     * 状态修改
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        return roleService.changeStatus(role);
    }

    /**
     * 删除角色
     */
    @RequiresPermissions("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return roleService.remove(roleIds);
    }

    /**
     * 获取角色选择框列表
     */
    @RequiresPermissions("system:role:query")
    @GetMapping("/option/select")
    public AjaxResult optionSelect() {
        return roleService.optionSelect();
    }

    /**
     * 查询已分配用户角色列表
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/authUser/allocatedList")
    public AjaxResult allocatedList(@RequestBody SysUser user) {
        return roleService.allocatedList(user);
    }

    /**
     * 查询未分配用户角色列表
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/authUser/unallocatedList")
    public AjaxResult unallocatedList(@RequestBody SysUser user) {
        return roleService.unallocatedList(user);
    }

    /**
     * 取消授权用户
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole) {
        return roleService.deleteAuthUser(userRole);
    }

    /**
     * 批量取消授权用户
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds) {
        return roleService.deleteAuthUsers(roleId, userIds);
    }

    /**
     * 批量选择用户授权
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds) {
        return roleService.selectAuthUserAll(roleId, userIds);
    }

    /**
     * 获取对应角色部门树列表
     */
    @RequiresPermissions("system:role:query")
    @GetMapping(value = "/deptTree/{roleId}")
    public AjaxResult deptTree(@PathVariable("roleId") Long roleId) {
        return roleService.deptTree(roleId);
    }
}
