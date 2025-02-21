package com.sse.sseapp.service;

import com.sse.sseapp.constant.ConstantFactory;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysMenu;
import com.sse.sseapp.feign.system.ISysMenuFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统管理-菜单管理 service
 *
 * @author zhengyaosheng
 * @date 2023/02/24
 **/
@Slf4j
@Service
public class SysMenuService {

    @Autowired
    private ISysMenuFeign sysMenuFeign;

    /**
     * 工厂类
     */
    @Autowired
    private ConstantFactory constantFactory;

    /**
     * 获取菜单列表
     */
    public AjaxResult list(SysMenu menu) {
        Long userId = constantFactory.getLoginUserInfo().getUserId();
        menu.setUserId(userId);
        return this.sysMenuFeign.list(menu);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    public AjaxResult getInfo(Long menuId) {
        return this.sysMenuFeign.getInfo(menuId);
    }

    /**
     * 获取菜单下拉树列表
     */
    public AjaxResult treeSelect(SysMenu menu) {
        Long userId = constantFactory.getLoginUserInfo().getUserId();
        menu.setUserId(userId);
        return this.sysMenuFeign.treeSelect(menu);
    }

    /**
     * 加载对应角色菜单列表树
     */
    public AjaxResult roleMenuTreeSelect(Long roleId) {
        Long userId = constantFactory.getLoginUserInfo().getUserId();
        return this.sysMenuFeign.roleMenuTreeSelect(roleId, userId);
    }

    /**
     * 新增菜单
     */
    public AjaxResult add(SysMenu menu) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        menu.setCreateBy(username);
        return this.sysMenuFeign.add(menu);
    }

    /**
     * 修改菜单
     */
    public AjaxResult edit(SysMenu menu) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        menu.setUpdateBy(username);
        return this.sysMenuFeign.edit(menu);
    }

    /**
     * 删除菜单
     */
    public AjaxResult remove(Long menuId) {
        return this.sysMenuFeign.remove(menuId);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    public AjaxResult getRouters() {
        Long userId = constantFactory.getLoginUserInfo().getUserId();
        return this.sysMenuFeign.getRouters(userId);
    }
}
