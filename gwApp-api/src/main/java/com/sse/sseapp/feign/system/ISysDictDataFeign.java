package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDictData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统管理-数据字典管理-feign
 *
 * @author zhengyaosheng
 * @data 2023-02-25
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/dict/data")
public interface ISysDictDataFeign {

    @PostMapping("/list")
    AjaxResult list(@RequestBody SysDictData dictData);

    /**
     * 查询字典数据详细
     */
    @GetMapping(value = "/query/{dictCode}")
    AjaxResult getInfo(@PathVariable(value = "dictCode") Long dictCode);

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    AjaxResult dictType(@PathVariable(value = "dictType") String dictType);

    /**
     * 新增字典类型
     */
    @PostMapping("/add")
    AjaxResult add(@Validated @RequestBody SysDictData dict);

    /**
     * 修改保存字典类型
     */
    @PutMapping("/edit")
    AjaxResult edit(@Validated @RequestBody SysDictData dict);

    /**
     * 删除字典类型
     */
    @DeleteMapping("/remove/{dictCodes}")
    AjaxResult remove(@PathVariable(value = "dictCodes") Long[] dictCodes);


    @RequestMapping("/getDictMap/{dictType}")
    List<Map<String,Object>> getDictMap(@PathVariable(value = "dictType") String dictType);
}
