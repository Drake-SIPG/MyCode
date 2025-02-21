package com.sse.sseapp.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.sse.sseapp.core.domain.PageParamDto;
import com.sse.sseapp.core.page.GcPageInfo;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProxyConfig;
import com.sse.sseapp.service.system.SysProxyConfigService;
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
@RequestMapping("/dataStore/system/proxy")
public class SysProxyController extends BaseController {
    @Autowired
    private SysProxyConfigService sysProxyConfigService;

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping(value = "/getInfo/{configCode}")
    public SysProxyConfig getInfo(@PathVariable(value = "configCode") String configCode) {
        return sysProxyConfigService.selectConfigByCode(configCode);
    }

    /**
     * 获取参数配置集合
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysProxyConfig sysProxyConfig) {
        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setPageNum(sysProxyConfig.getCurrent());
        pageParamDto.setPageSize(sysProxyConfig.getPageSize());
        List<SysProxyConfig> list = sysProxyConfigService.selectSysProxyConfigList(sysProxyConfig);
        GcPageInfo gcPageInfo = new GcPageInfo(list, pageParamDto);
        return AjaxResult.success(gcPageInfo);
    }

    /**
     * 根据id获取详细信息
     */
    @GetMapping(value = "/query/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        if (ObjectUtil.isNull(sysProxyConfigService.selectSysProxyConfigById(id))){
            return error("没有找到对应信息");
        }
        return success(sysProxyConfigService.selectSysProxyConfigById(id));
    }

    /**
     * 新增参数配置
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysProxyConfig sysProxyConfig) {
        if (ObjectUtil.isNotEmpty(sysProxyConfig)){
            if (ObjectUtil.isNotEmpty(sysProxyConfigService.selectConfigByCode(sysProxyConfig.getCode()))){
                return error("该code已存在");
            }
            return toAjax(sysProxyConfigService.insertSysProxyConfig(sysProxyConfig));
        }
        return error("新增数据为空");
    }

    /**
     * 修改保存参数配置
     */
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysProxyConfig sysProxyConfig) {
        return toAjax(sysProxyConfigService.updateSysProxyConfig(sysProxyConfig));
    }

    /**
     * 批量删除字典类型
     */
    @DeleteMapping("/remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        sysProxyConfigService.deleteSysProxyConfigByIds(ids);
        return success();
    }

}
