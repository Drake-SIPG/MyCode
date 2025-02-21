package com.sse.sseapp.controller;

import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysVideoConfig;
import com.sse.sseapp.feign.system.ISysVideoConfigFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参数配置 信息操作处理
 *
 * @author zhengyaosheng
 * @date 2021-02-23
 */
@RestController
@RequestMapping("/system/video/config")
public class SysVideoConfigController extends BaseController {
    @Autowired
    private ISysVideoConfigFeign sysVideoConfigFeign;

    /**
     * 获取视频配置树
     */
    @PostMapping("/tree/{rootId}")
    public AjaxResult tree(@PathVariable(value = "rootId") Long rootId) {
        return this.sysVideoConfigFeign.tree(rootId);
    }

    /**
     * 获取视频配置树
     */
    @PostMapping("/list")
    public AjaxResult list() {
        return this.sysVideoConfigFeign.list();
    }

    /**
     * 新增视频配置
     *
     * @param config 请求参数
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysVideoConfig config) {
        return this.sysVideoConfigFeign.add(config);
    }

    /**
     * 修改视频配置
     *
     * @param config 请求参数
     */
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysVideoConfig config) {
        return this.sysVideoConfigFeign.edit(config);
    }

    /**
     * 删除视频配置
     *
     * @param configIds 请求参数
     */
    @PostMapping("/remove/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds) {
        return this.sysVideoConfigFeign.remove(configIds);
    }

    /**
     * 查询详情
     *
     * @param configId
     * @return
     */
    @PostMapping("/query/{configId}")
    public AjaxResult query(@PathVariable(value = "configId") Long configId) {
        return this.sysVideoConfigFeign.query(configId);
    }
}
