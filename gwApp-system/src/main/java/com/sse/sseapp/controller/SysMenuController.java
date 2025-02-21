package com.sse.sseapp.controller;

import com.sse.sseapp.core.annotation.Log;
import com.sse.sseapp.core.enums.BusinessType;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysMenu;
import com.sse.sseapp.security.annotation.RequiresPermissions;
import com.sse.sseapp.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 菜单信息
 *
 * @author zhengyaosheng
 * @date 2021-02-23
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {
    @Autowired
    private SysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @RequiresPermissions("system:menu:list")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysMenu menu) {
        return menuService.list(menu);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @RequiresPermissions("system:menu:query")
    @GetMapping(value = "/query/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId) {
        return menuService.getInfo(menuId);
    }

    /**
     * 获取菜单下拉树列表
     */
    @PostMapping("/treeSelect")
    public AjaxResult treeSelect(@RequestBody SysMenu menu) {
        return menuService.treeSelect(menu);
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeSelect/{roleId}")
    public AjaxResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        return menuService.roleMenuTreeSelect(roleId);
    }

    /**
     * 新增菜单
     */
    @RequiresPermissions("system:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        return menuService.add(menu);
    }

    /**
     * 修改菜单
     */
    @RequiresPermissions("system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        return menuService.edit(menu);
    }

    /**
     * 删除菜单
     */
    @RequiresPermissions("system:menu:remove")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        return menuService.remove(menuId);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public AjaxResult getRouters() {
        return menuService.getRouters();
    }
}