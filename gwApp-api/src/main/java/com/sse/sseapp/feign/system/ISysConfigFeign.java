package com.sse.sseapp.feign.system;


import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统管理-配置管理-feign
 *
 * @author zhengyaosheng
 * @data 2023-02-25
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/config")
public interface ISysConfigFeign {

    /**
     * 获取参数配置列表
     *
     * @param config 请求参数
     */
    @PostMapping("/list")
    AjaxResult list(@RequestBody SysConfig config);

    /**
     * 根据参数编号获取详细信息
     *
     * @param configId 请求参数
     */
    @GetMapping(value = "/getInfo/{configId}")
    SysConfig getInfo(@PathVariable(value = "configId") Long configId);

    /**
     * 根据参数键名查询参数值
     *
     * @param configKey 请求参数
     */
    @GetMapping(value = "/configKey/{configKey}")
    String getConfigKey(@PathVariable(value = "configKey") String configKey);

    /**
     * 新增参数配置
     */
    @PostMapping("/add")
    AjaxResult add(@Validated @RequestBody SysConfig config);

    /**
     * 修改参数配置
     */
    @PutMapping("/edit")
    AjaxResult edit(@Validated @RequestBody SysConfig config);

    /**
     * 删除参数配置
     */
    @DeleteMapping("/remove/{configIds}")
    AjaxResult remove(@PathVariable(value = "configIds") Long[] configIds);

    /**
     * 刷新参数缓存
     */
    @DeleteMapping("/refreshCache")
    void refreshCache();
}
