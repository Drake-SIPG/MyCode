package com.sse.sseapp.service;

import com.sse.sseapp.constant.ConstantFactory;
import com.sse.sseapp.domain.system.SysConfig;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.core.web.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统管理-参数配置 service
 *
 * @author zhengyaosheng
 * @date 2023/02/23
 **/
@Slf4j
@Service
public class SysConfigService {

    @Autowired
    private ISysConfigFeign sysConfigFeign;

    /**
     * 工厂类
     */
    @Autowired
    private ConstantFactory constantFactory;

    /**
     * 列表获取
     *
     * @param config 请求参数
     */
    public AjaxResult list(SysConfig config) {
        AjaxResult tableDataInfo = this.sysConfigFeign.list(config);
        return AjaxResult.success(tableDataInfo);
    }

    /**
     * 查询详情
     */

    public AjaxResult query(Long configId) {
        SysConfig sysConfig = this.sysConfigFeign.getInfo(configId);
        return AjaxResult.success(sysConfig);
    }

    /**
     * 根据参数键名查询参数值
     *
     * @param configKey 请求参数
     */
    public AjaxResult configKey(String configKey) {
        String result = this.sysConfigFeign.getConfigKey(configKey);
        return AjaxResult.success(result);
    }

    /**
     * 添加
     *
     * @param config 请求参数
     */
    public AjaxResult insertConfig(SysConfig config) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        config.setCreateBy(username);
        return this.sysConfigFeign.add(config);

    }

    /**
     * 更新
     *
     * @param config 请求参数
     */
    public AjaxResult update(SysConfig config) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        config.setUpdateBy(username);
        return this.sysConfigFeign.edit(config);
    }

    /**
     * 删除
     *
     * @param configIds 请求参数
     */
    public AjaxResult deleteConfigByIds(Long[] configIds) {
        return this.sysConfigFeign.remove(configIds);
    }

    /**
     * 刷新
     */
    public void refreshCache() {
        this.sysConfigFeign.refreshCache();
    }


}
