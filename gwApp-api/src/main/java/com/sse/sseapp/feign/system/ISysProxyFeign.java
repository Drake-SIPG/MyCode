package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProxyConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统管理-配置管理-feign
 *
 * @author zhengyaosheng
 * @date 2023-02-25
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/proxy")
public interface ISysProxyFeign {
    /**
     * 根据代理编码获取代理参数
     *
     * @param configCode 请求参数
     */
    @GetMapping(value = "/getInfo/{configCode}")
    SysProxyConfig getInfo(@PathVariable(value = "configCode") String configCode);

    /**
     * 获取参数配置集合
     */
    @PostMapping("/list")
    AjaxResult list(@RequestBody SysProxyConfig sysProxyConfig);

    /**
     * 根据id获取详细信息
     */
    @GetMapping(value = "/query/{id}")
    AjaxResult getInfo(@PathVariable(value = "id") Long id);

    /**
     * 新增参数配置
     */
    @PostMapping("/add")
    AjaxResult add(@Validated @RequestBody SysProxyConfig sysProxyConfig);

    /**
     * 修改保存参数配置
     */
    @PutMapping("/edit")
    AjaxResult edit(@Validated @RequestBody SysProxyConfig sysProxyConfig);

    /**
     * 批量删除字典类型
     */
    @DeleteMapping("/remove/{ids}")
    AjaxResult remove(@PathVariable(value = "ids") Long[] ids);
}
