package com.sse.sseapp.service;

import com.sse.sseapp.constant.ConstantFactory;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysRole;
import com.sse.sseapp.domain.system.SysUser;
import com.sse.sseapp.domain.system.SysUserRole;
import com.sse.sseapp.feign.system.ISysRoleFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统管理-角色管理 service
 *
 * @author zhengyaosheng
 * @date 2023/02/24
 **/
@Slf4j
@Service
public class SysRoleService {

    @Autowired
    private ISysRoleFeign sysRoleFeign;

    /**
     * 工厂类
     */
    @Autowired
    private ConstantFactory constantFactory;

    /**
     * 列表查询
     *
     */
    public AjaxResult list(SysRole role) {
        return this.sysRoleFeign.list(role);
    }

    /**
     * 根据角色编号获取详细信息
     */
    public AjaxResult getInfo(Long roleId) {
        return this.sysRoleFeign.getInfo(roleId);
    }

    /**
     * 新增角色
     */
    public AjaxResult add(SysRole role) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        role.setCreateBy(username);
        return this.sysRoleFeign.add(role);
    }

    /**
     * 修改保存角色
     */
    public AjaxResult edit(SysRole role) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        role.setUpdateBy(username);
        return this.sysRoleFeign.edit(role);
    }

    /**
     * 修改保存数据权限
     */
    public AjaxResult dataScope(SysRole role) {
        return this.sysRoleFeign.dataScope(role);
    }

    /**
     * 状态修改
     */
    public AjaxResult changeStatus(SysRole role) {
        AjaxResult result = this.sysRoleFeign.changeStatus(role);
        log.info("结果为：{}", result);
        log.info("角色状态修改结束");
        return result;
    }

    /**
     * 删除角色
     */
    public AjaxResult remove(Long[] roleIds) {
        return this.sysRoleFeign.remove(roleIds);
    }

    /**
     * 获取角色选择框列表
     */
    public AjaxResult optionSelect() {
        return this.sysRoleFeign.optionSelect();
    }

    /**
     * 查询已分配用户角色列表
     */
    public AjaxResult allocatedList(SysUser user) {
        return this.sysRoleFeign.allocatedList(user);
    }

    /**
     * 查询未分配用户角色列表
     */
    public AjaxResult unallocatedList(SysUser user) {
        return this.sysRoleFeign.unallocatedList(user);
    }

    /**
     * 取消授权用户
     */
    public AjaxResult deleteAuthUser(SysUserRole userRole) {
        return this.sysRoleFeign.cancelAuthUser(userRole);
    }

    /**
     * 批量取消授权用户
     */
    public AjaxResult deleteAuthUsers(Long roleId, Long[] userIds) {
        return this.sysRoleFeign.cancelAuthUserAll(roleId, userIds);
    }

    /**
     * 批量选择用户授权
     */
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds) {
        return this.sysRoleFeign.selectAuthUserAll(roleId, userIds);
    }

    /**
     * 获取对应角色部门树列表
     */
    public AjaxResult deptTree(Long roleId) {
        return this.sysRoleFeign.deptTree(roleId);
    }
}
