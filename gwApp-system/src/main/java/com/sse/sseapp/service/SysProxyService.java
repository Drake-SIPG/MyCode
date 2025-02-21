package com.sse.sseapp.service;


import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProxyConfig;
import com.sse.sseapp.feign.system.ISysProxyFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author liuxinyu
 * @date 2023/03/28
 */
@Slf4j
@Service
public class SysProxyService {

    @Autowired
    private ISysProxyFeign iSysProxyFeign;

    /**
     * 根据参数编号获取详细信息
     */
    public SysProxyConfig getInfo(String configCode) {
        return iSysProxyFeign.getInfo(configCode);
    }

    /**
     * 获取参数配置集合
     */
    public AjaxResult list(SysProxyConfig sysProxyConfig) {
        return iSysProxyFeign.list(sysProxyConfig);
    }

    /**
     * 根据id获取详细信息
     */
    public AjaxResult getInfo(Long id) {
        return iSysProxyFeign.getInfo(id);
    }

    /**
     * 新增参数配置
     */
    public AjaxResult add(SysProxyConfig sysProxyConfig) {
        return iSysProxyFeign.add(sysProxyConfig);
    }

    /**
     * 修改保存参数配置
     */
    public AjaxResult edit(SysProxyConfig sysProxyConfig) {
        return iSysProxyFeign.edit(sysProxyConfig);
    }

    /**
     * 批量删除字典类型
     */
    public AjaxResult remove(Long[] ids) {
        return iSysProxyFeign.remove(ids);
    }
}
