package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDictType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 数据字典类型
 *
 * @author zhengyaosheng
 * @date 2023-03-01
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/dict/type")
public interface ISysDictTypeFeign {

    /**
     * 列表获取
     *
     * @param dictType
     * @return
     */
    @PostMapping("/list")
    AjaxResult list(@RequestBody SysDictType dictType);

    /**
     * 查询字典类型详细
     */
    @GetMapping(value = "/query/{dictId}")
    AjaxResult getInfo(@PathVariable(value = "dictId") Long dictId);

    /**
     * 查询字典类型详细
     */
    @GetMapping(value = "/getInfoByType/{dictType}")
    SysDictType getInfoByType(@PathVariable(value = "dictType") String dictType);

    /**
     * 新增字典类型
     */
    @PostMapping("/add")
    AjaxResult add(@Validated @RequestBody SysDictType dict);

    /**
     * 修改字典类型
     */
    @PutMapping("/edit")
    AjaxResult edit(@Validated @RequestBody SysDictType dict);

    /**
     * 删除字典类型
     */
    @DeleteMapping("/remove/{dictIds}")
    AjaxResult remove(@PathVariable(value = "dictIds") Long[] dictIds);

    /**
     * 刷新字典缓存
     */
    @DeleteMapping("/refreshCache")
    AjaxResult refreshCache();

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionSelect")
    AjaxResult optionSelect();
}
