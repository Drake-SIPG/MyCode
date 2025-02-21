package com.sse.sseapp.controller;

import com.sse.sseapp.core.annotation.Log;
import com.sse.sseapp.core.enums.BusinessType;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDictData;
import com.sse.sseapp.security.annotation.RequiresPermissions;
import com.sse.sseapp.service.SysDictDataService;
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
@RequestMapping("/system/dict/data")
public class SysDictDataController {

    @Autowired
    private SysDictDataService sysDictDataService;


    @RequiresPermissions("system:dict:list")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysDictData dictData) {
        return sysDictDataService.list(dictData);
    }

    /**
     * 查询字典数据详细
     */
    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/query/{dictCode}")
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        return sysDictDataService.getInfo(dictCode);
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType) {
        return sysDictDataService.dictType(dictType);
    }

    /**
     * 新增字典类型
     */
    @RequiresPermissions("system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        return sysDictDataService.add(dict);
    }

    /**
     * 修改保存字典类型
     */
    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysDictData dict) {
        return sysDictDataService.edit(dict);
    }

    /**
     * 删除字典类型
     */
    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        return sysDictDataService.remove(dictCodes);
    }
}
