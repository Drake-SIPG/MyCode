package com.sse.sseapp.service;

import com.sse.sseapp.constant.ConstantFactory;
import com.sse.sseapp.core.domain.R;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDept;
import com.sse.sseapp.domain.system.SysUser;
import com.sse.sseapp.domain.system.model.LoginUser;
import com.sse.sseapp.feign.system.ISysUserFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统管理-用户管理 service
 *
 * @author zhengyaosheng
 * @date 2023/02/24
 **/
@Slf4j
@Service
public class SysUserService {

    @Autowired
    private ISysUserFeign sysUserFeign;

    /**
     * 工厂类
     */
    @Autowired
    private ConstantFactory constantFactory;

    /**
     * 列表获取
     *
     */
    public AjaxResult list(SysUser user) {
        return this.sysUserFeign.list(user);
    }

    /**
     * 根据用户名称获取用户信息
     *
     */
    public R<LoginUser> getUserByUsername(String username) {
        return sysUserFeign.info(username);
    }

    /**
     * 注册用户信息
     *
     */
    public R<Boolean> registerUser(SysUser sysUser) {
        return sysUserFeign.register(sysUser);
    }

    /**
     * 获取用户信息
     *
     */
    public AjaxResult getInfo() {
        Long userId = constantFactory.getLoginUserInfo().getUserId();
        return sysUserFeign.getInfo(userId);
    }

    /**
     * 获取用户信息根据id
     *
     */
    public AjaxResult getUserByUserId(Long userId) {
        return sysUserFeign.query(userId);
    }

    /**
     * 添加用户
     *
     */
    public AjaxResult addUser(SysUser user) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        user.setCreateBy(username);
        return sysUserFeign.add(user);
    }

    /**
     * 修改用户
     *
     */
    public AjaxResult updateUser(SysUser user) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        user.setUpdateBy(username);
        return sysUserFeign.edit(user);
    }

    /**
     * 删除用户
     *
     */
    public AjaxResult deleteUserByIds(Long[] userIds) {
        Long userId = constantFactory.getLoginUserInfo().getUserId();
        return sysUserFeign.remove(userIds, userId);
    }

    /**
     * 重置密码
     *
     */
    public AjaxResult resetPwd(SysUser user) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        user.setUpdateBy(username);
        return sysUserFeign.resetPwd(user);
    }

    /**
     * 修改状态
     *
     */
    public AjaxResult changeStatus(SysUser user) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        user.setUpdateBy(username);
        return sysUserFeign.changeStatus(user);
    }

    /**
     * 根据用户编号获取授权角色
     *
     */
    public AjaxResult authRole(Long userId) {
        return sysUserFeign.authRole(userId);
    }

    /**
     * 用户批量授权角色
     *
     */
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        return sysUserFeign.insertAuthRole(userId, roleIds);
    }

    /**
     * 获取部门树
     *
     */
    public AjaxResult deptTree(SysDept dept) {
        return sysUserFeign.deptTree(dept);
    }
}
