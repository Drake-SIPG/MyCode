package com.sse.sseapp.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.sse.sseapp.core.domain.PageParamDto;
import com.sse.sseapp.core.page.GcPageInfo;
import com.sse.sseapp.domain.system.SysDept;
import com.sse.sseapp.domain.system.SysRole;
import com.sse.sseapp.domain.system.SysUser;
import com.sse.sseapp.domain.system.SysUserRole;
import com.sse.sseapp.core.constant.UserConstants;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.service.system.ISysConfigService;
import com.sse.sseapp.utils.SecurityUtils;
import com.sse.sseapp.service.system.ISysDeptService;
import com.sse.sseapp.service.system.ISysRoleService;
import com.sse.sseapp.service.system.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色信息
 *
 * @author sse
 */
@RestController
@RequestMapping("/dataStore/system/role")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 列表获取
     *
     * @param role
     * @return
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysRole role) {
        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setPageNum(role.getPageNum());
        pageParamDto.setPageSize(role.getPageSize());
        List<SysRole> list = roleService.selectRoleList(role);
        GcPageInfo gcPageInfo = new GcPageInfo(list, pageParamDto);
        return AjaxResult.success(gcPageInfo);
    }

    /**
     * 根据角色编号获取详细信息
     */
    @GetMapping(value = "/query/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 修改保存数据权限
     */
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/remove/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @GetMapping("/optionSelect")
    public AjaxResult optionSelect() {
        return success(roleService.selectRoleAll());
    }

    /**
     * 查询已分配用户角色列表
     */
    @PostMapping("/authUser/allocatedList")
    public AjaxResult allocatedList(@RequestBody SysUser user) {
        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setPageNum(user.getPageNum());
        pageParamDto.setPageSize(user.getPageSize());
        List<SysUser> list = userService.selectAllocatedList(user);
        GcPageInfo gcPageInfo = new GcPageInfo(list, pageParamDto);
        return AjaxResult.success(gcPageInfo);
    }

    /**
     * 查询未分配用户角色列表
     */
    @PostMapping("/authUser/unallocatedList")
    public AjaxResult unallocatedList(@RequestBody SysUser user) {
        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setPageNum(user.getPageNum());
        pageParamDto.setPageSize(user.getPageSize());
        List<SysUser> list = userService.selectUnallocatedList(user);
        GcPageInfo gcPageInfo = new GcPageInfo(list, pageParamDto);
        return AjaxResult.success(gcPageInfo);
    }

    /**
     * 取消授权用户
     */
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     */
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户授权
     */
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds) {
        for (Long userId : userIds) {
            List<SysRole> roles = roleService.selectRolesByUserId(userId);
            Map<Long, SysRole> roleMap = roles.stream().collect(Collectors.toMap(SysRole::getRoleId, SysRole -> SysRole));
            if (ObjectUtil.isNotEmpty(roles)) {
                List<String> roleRepelList = Arrays.asList(configService.selectConfigByKey("roleRepel").split(","));
                for (SysRole sysRole : roles) {
                    if (sysRole.isFlag() && roleRepelList.contains(sysRole.getRoleKey() + ":" + roleMap.get(roleId).getRoleKey())
                        || sysRole.isFlag() && roleRepelList.contains(roleMap.get(roleId).getRoleKey() + ":" + sysRole.getRoleKey())) {
                            return AjaxResult.error("授权用户角色与角色互斥");
                    }
                }
            }
        }
        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 获取对应角色部门树列表
     */
    @GetMapping(value = "/deptTree/{roleId}")
    public AjaxResult deptTree(@PathVariable("roleId") Long roleId) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.selectDeptTreeList(new SysDept()));
        return ajax;
    }

}
