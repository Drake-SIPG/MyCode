package com.sse.sseapp.controller.push;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.core.constant.UserConstants;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.push.AppPushConfig;
import com.sse.sseapp.service.push.AppPushConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-06-07
 */
@RestController
@RequestMapping("/dataStore/push/appPushConfig")
public class AppPushConfigController extends BaseController {

    @Autowired
    private AppPushConfigService appPushConfigService;

    /**
     * 获取参数配置列表
     */
    @PostMapping("/list")
    public Page<AppPushConfig> list(@RequestBody AppPushConfig config) {
        return appPushConfigService.selectConfigList(config);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping(value = "/getInfo/{configId}")
    public AppPushConfig getInfo(@PathVariable(value = "configId") String configId) {
        return this.appPushConfigService.selectConfigById(configId);
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public String getConfigKey(@PathVariable(value = "configKey") String configKey) {
        return this.appPushConfigService.selectConfigByKey(configKey);
    }

    /**
     * 新增参数配置
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody AppPushConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(appPushConfigService.checkConfigKeyUnique(config))) {
            return error("新增推送参数'" + config.getPushConfigName() + "'失败，参数键名已存在");
        }
        return toAjax(appPushConfigService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody AppPushConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(appPushConfigService.checkConfigKeyUnique(config))) {
            return error("修改推送参数'" + config.getPushConfigName() + "'失败，参数键名已存在");
        }
        return toAjax(appPushConfigService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @DeleteMapping("/remove/{configIds}")
    public AjaxResult remove(@PathVariable(value = "configIds") String[] configIds) {
        return appPushConfigService.deleteConfigByIds(configIds);
    }

    /**
     * 刷新参数缓存
     */
    @DeleteMapping("/refreshCache")
    public void refreshCache() {
        appPushConfigService.resetConfigCache();
    }

}
