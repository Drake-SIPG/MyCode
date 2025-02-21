package com.sse.sseapp.controller.system;


import com.sse.sseapp.core.constant.UserConstants;
import com.sse.sseapp.core.domain.PageParamDto;
import com.sse.sseapp.core.page.GcPageInfo;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.core.web.page.TableDataInfo;
import com.sse.sseapp.domain.system.SysConfig;
import com.sse.sseapp.service.system.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 参数配置 信息操作处理
 *
 * @author sse
 */
@RestController
@RequestMapping("/dataStore/system/config")
public class SysConfigController extends BaseController {
    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysConfig config) {
        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setPageNum(config.getPageNum());
        pageParamDto.setPageSize(config.getPageSize());
        List<SysConfig> list = configService.selectConfigList(config);
        GcPageInfo gcPageInfo = new GcPageInfo(list, pageParamDto);
        return AjaxResult.success(gcPageInfo);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping(value = "/getInfo/{configId}")
    public SysConfig getInfo(@PathVariable(value = "configId") Long configId) {
        return this.configService.selectConfigById(configId);
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public String getConfigKey(@PathVariable(value = "configKey") String configKey) {
        return this.configService.selectConfigByKey(configKey);
    }

    /**
     * 新增参数配置
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toAjax(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toAjax(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @DeleteMapping("/remove/{configIds}")
    public AjaxResult remove(@PathVariable(value = "configIds") Long[] configIds) {
        return configService.deleteConfigByIds(configIds);
    }

    /**
     * 刷新参数缓存
     */
    @DeleteMapping("/refreshCache")
    public void refreshCache() {
        configService.resetConfigCache();
    }
}
