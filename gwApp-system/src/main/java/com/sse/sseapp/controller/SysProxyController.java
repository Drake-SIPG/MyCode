package com.sse.sseapp.controller;



import com.sse.sseapp.core.annotation.Log;
import com.sse.sseapp.core.enums.BusinessType;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProxyConfig;
import com.sse.sseapp.security.annotation.RequiresPermissions;

import com.sse.sseapp.service.SysProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



/**
 * @author liuxinyu
 * @date 2023-03-28
 */

@RestController
@RequestMapping("/system/proxy")
public class SysProxyController {
    @Autowired
    private SysProxyService proxyService;

    /**
     * 根据参数编号获取详细信息
     */
    @RequiresPermissions("system:proxy:getInfo")
    @GetMapping(value = "/getInfo/{configCode}")
    public SysProxyConfig getInfo(@PathVariable(value = "configCode") String configCode) {
        return proxyService.getInfo(configCode);
    }

    /**
     * 获取参数配置集合
     */
    @RequiresPermissions("system:proxy:list")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysProxyConfig sysProxyConfig) {
        return proxyService.list(sysProxyConfig);
    }

    /**
     * 根据id获取详细信息
     */
    @RequiresPermissions("system:proxy:query")
    @GetMapping(value = "/query/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return proxyService.getInfo(id);
    }

    /**
     * 新增参数配置
     */
    @RequiresPermissions("system:proxy:add")
    @PostMapping("/add")
    @Log(title = "参数配置", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysProxyConfig sysProxyConfig) {
        return proxyService.add(sysProxyConfig);
    }

    /**
     * 修改保存参数配置
     */
    @RequiresPermissions("system:proxy:edit")
    @PutMapping("/edit")
    @Log(title = "参数配置", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody SysProxyConfig sysProxyConfig) {
        return proxyService.edit(sysProxyConfig);
    }

    /**
     * 批量删除字典类型
     */
    @RequiresPermissions("system:proxy:remove")
    @DeleteMapping("/remove/{ids}")
    @Log(title = "参数配置", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return proxyService.remove(ids);
    }

}
