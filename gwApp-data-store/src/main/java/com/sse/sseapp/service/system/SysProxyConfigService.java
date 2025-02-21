package com.sse.sseapp.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.system.SysProxyConfig;

import java.util.List;

/**
 * <p>
 * 第三方接口配置表 服务类
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-27
 */
public interface SysProxyConfigService extends IService<SysProxyConfig> {

    SysProxyConfig selectConfigByCode(String configCode);

    List<SysProxyConfig> selectSysProxyConfigList(SysProxyConfig proxyConfig);

    SysProxyConfig selectSysProxyConfigById(Long sysProxyConfigId);

    int insertSysProxyConfig(SysProxyConfig sysProxyConfig);

    int updateSysProxyConfig(SysProxyConfig sysProxyConfig);

    void deleteSysProxyConfigByIds(Long[] sysProxyConfigIds);

}
