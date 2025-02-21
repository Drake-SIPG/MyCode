package com.sse.sseapp.controller;


import com.sse.sseapp.domain.system.SysConfig;
import com.sse.sseapp.core.annotation.Log;
import com.sse.sseapp.core.enums.BusinessType;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.security.annotation.RequiresPermissions;
import com.sse.sseapp.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 参数配置 信息操作处理
 * 
 * @author zhengyaosheng
 * @date 2021-02-23
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
    @Autowired
    private SysConfigService configService;

    /**
     * 获取参数配置列表
     *
     * @param config 请求参数
     */
    @RequiresPermissions("system:config:list")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysConfig config) {
        return this.configService.list(config);
    }

    /**
     * 根据参数编号获取详细信息
     *
     * @param configId 请求参数
     */
    @GetMapping(value = "/query/{configId}")
    public AjaxResult getInfo(@PathVariable Long configId) {
        return this.configService.query(configId);
    }

    /**
     * 根据参数键名查询参数值
     *
     * @param configKey 请求参数
     */
    @GetMapping(value = "/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey) {
        return this.configService.configKey(configKey);
    }

    /**
     * 新增参数配置
     *
     * @param config 请求参数
     */
    @RequiresPermissions("system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysConfig config) {
        return this.configService.insertConfig(config);
    }

    /**
     * 修改参数配置
     *
     * @param config 请求参数
     */
    @RequiresPermissions("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysConfig config) {
        return this.configService.update(config);
    }

    /**
     * 删除参数配置
     *
     * @param configIds 请求参数
     */
    @RequiresPermissions("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds) {
        return this.configService.deleteConfigByIds(configIds);
    }

    /**
     * 刷新参数缓存
     */
    @RequiresPermissions("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        this.configService.refreshCache();
        return success();
    }
}
