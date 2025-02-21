package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysMenu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统管理-菜单管理-feign
 *
 * @author zhengyaosheng
 * @data 2023-02-25
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/menu")
public interface ISysMenuFeign {

    /**
     * 获取菜单列表
     */
    @PostMapping("/list")
    AjaxResult list(@RequestBody SysMenu menu);

    /**
     * 根据菜单编号获取详细信息
     */
    @GetMapping(value = "/query/{menuId}")
    AjaxResult getInfo(@PathVariable(value = "menuId") Long menuId);

    /**
     * 获取菜单下拉树列表
     */
    @PostMapping("/treeSelect")
    AjaxResult treeSelect(@RequestBody SysMenu menu);

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeSelect")
    AjaxResult roleMenuTreeSelect(@RequestParam("roleId") Long roleId, @RequestParam("userId") Long userId);

    /**
     * 新增菜单
     */
    @PostMapping("/add")
    AjaxResult add(@Validated @RequestBody SysMenu menu);

    /**
     * 修改菜单
     */
    @PutMapping("/edit")
    AjaxResult edit(@Validated @RequestBody SysMenu menu);

    /**
     * 删除菜单
     */
    @DeleteMapping("/remove/{menuId}")
    AjaxResult remove(@PathVariable("menuId") Long menuId);

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters/{userId}")
    AjaxResult getRouters(@PathVariable("userId") Long userId);
}
