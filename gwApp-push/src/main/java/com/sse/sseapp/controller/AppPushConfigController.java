package com.sse.sseapp.controller;


import com.sse.sseapp.core.annotation.Log;
import com.sse.sseapp.core.constant.ResponseBean;
import com.sse.sseapp.core.enums.BusinessType;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.push.AppPushConfig;
import com.sse.sseapp.security.annotation.RequiresPermissions;
import com.sse.sseapp.service.AppPushConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/push/appPushConfig")
public class AppPushConfigController extends BaseController {
    
    @Autowired
    private AppPushConfigService appPushConfigService;

    /**
     * 获取参数配置列表
     *
     * @param config 请求参数
     */
    @PostMapping("/list")
    public ResponseBean list(@RequestBody AppPushConfig config) {
        return this.appPushConfigService.list(config);
    }

    /**
     * 根据参数编号获取详细信息
     *
     * @param configId 请求参数
     */
    @GetMapping(value = "/query/{configId}")
    public ResponseBean getInfo(@PathVariable String configId) {
        return this.appPushConfigService.query(configId);
    }

    /**
     * 根据参数键名查询参数值
     *
     * @param configKey 请求参数
     */
    @GetMapping(value = "/configKey/{configKey}")
    public ResponseBean getConfigKey(@PathVariable String configKey) {
        return this.appPushConfigService.configKey(configKey);
    }

    /**
     * 新增参数配置
     *
     * @param config 请求参数
     */
    @RequiresPermissions("system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody AppPushConfig config) {
        return this.appPushConfigService.insertConfig(config);
    }

    /**
     * 修改参数配置
     *
     * @param config 请求参数
     */
    @RequiresPermissions("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody AppPushConfig config) {
        return this.appPushConfigService.update(config);
    }

    /**
     * 删除参数配置
     *
     * @param configIds 请求参数
     */
    @RequiresPermissions("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{configIds}")
    public AjaxResult remove(@PathVariable String[] configIds) {
        return this.appPushConfigService.deleteConfigByIds(configIds);
    }

    /**
     * 刷新参数缓存
     */
    @RequiresPermissions("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public ResponseBean refreshCache() {
        this.appPushConfigService.refreshCache();
        return ResponseBean.success("成功");
    }
    
    
}
