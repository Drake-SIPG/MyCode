package com.sse.sseapp.controller;

import com.sse.sseapp.core.annotation.Log;
import com.sse.sseapp.core.enums.BusinessType;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDictType;
import com.sse.sseapp.security.annotation.RequiresPermissions;
import com.sse.sseapp.service.SysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 数据字典类型
 *
 * @author zhengyaosheng
 * @date 2023-03-01
 */
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController {

    @Autowired
    private SysDictTypeService sysDictTypeService;


    @RequiresPermissions("system:dict:list")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysDictType dictType) {
        return sysDictTypeService.list(dictType);
    }

    /**
     * 查询字典类型详细
     */
    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/query/{dictId}")
    public AjaxResult getInfo(@PathVariable Long dictId) {
        return sysDictTypeService.query(dictId);
    }

    /**
     * 新增字典类型
     */
    @RequiresPermissions("system:dict:add")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysDictType dict) {
        return sysDictTypeService.add(dict);
    }

    /**
     * 修改字典类型
     */
    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysDictType dict) {
        return sysDictTypeService.edit(dict);
    }

    /**
     * 删除字典类型
     */
    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{dictIds}")
    public AjaxResult remove(@PathVariable Long[] dictIds) {
        return sysDictTypeService.remove(dictIds);
    }

    /**
     * 刷新字典缓存
     */
    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        return sysDictTypeService.refreshCache();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionSelect")
    public AjaxResult optionSelect() {
        return sysDictTypeService.optionSelect();
    }
}
