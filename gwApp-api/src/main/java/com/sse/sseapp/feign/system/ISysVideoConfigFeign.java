package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysVideoConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 系统管理-视频配置管理-feign
 *
 * @author zhengyaosheng
 * @since 2023-02-25
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/video/config")
public interface ISysVideoConfigFeign {

    /**
     * 获取视频配置树
     */
    @PostMapping("/tree/{rootId}")
    AjaxResult tree(@PathVariable(value = "rootId") Long rootId);

    /**
     * 获取视频配置列表
     */
    @PostMapping("/list")
    AjaxResult list();

    /**
     * 新增参数配置
     */
    @PostMapping("/add")
    AjaxResult add(@Validated @RequestBody SysVideoConfig config);

    /**
     * 修改参数配置
     */
    @PostMapping("/edit")
    AjaxResult edit(@Validated @RequestBody SysVideoConfig config);

    /**
     * 删除参数配置
     */
    @PostMapping("/remove/{configIds}")
    AjaxResult remove(@PathVariable(value = "configIds") Long[] configIds);

    /**
     * 查询详情
     *
     * @param configId
     * @return
     */
    @PostMapping("/query/{configId}")
    AjaxResult query(@PathVariable(value = "configId") Long configId);
}
