package com.sse.sseapp.feign.push;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.push.AppPushConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 推送管理-队列信息管理-feign
 *
 * @author zhengyaosheng
 * @data 2023-06-07
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/push/appPushConfig")
public interface IAppPushConfigFeign {

    /**
     * 获取参数配置列表
     */
    @PostMapping("/list")
    Page<AppPushConfig> list(@RequestBody AppPushConfig config);

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping(value = "/getInfo/{configId}")
    AppPushConfig getInfo(@PathVariable(value = "configId") String configId);

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    String getConfigKey(@PathVariable(value = "configKey") String configKey);

    /**
     * 新增参数配置
     */
    @PostMapping("/add")
    AjaxResult add(@Validated @RequestBody AppPushConfig config);

    /**
     * 修改参数配置
     */
    @PutMapping("/edit")
    AjaxResult edit(@Validated @RequestBody AppPushConfig config);

    /**
     * 删除参数配置
     */
    @DeleteMapping("/remove/{configIds}")
    AjaxResult remove(@PathVariable(value = "configIds") String[] configIds);

    /**
     * 刷新参数缓存
     */
    @DeleteMapping("/refreshCache")
    void refreshCache();
}
