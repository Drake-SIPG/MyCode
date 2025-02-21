package com.sse.sseapp.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.system.SysProxyConfig;

import java.util.List;

/**
 * <p>
 * 第三方接口配置表 Mapper 接口
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-27
 */
public interface SysProxyConfigMapper extends BaseMapper<SysProxyConfig> {
    /**
     *
     * @param configCode
     * @return
     */
    SysProxyConfig selectConfigByCode(String configCode);

    List<SysProxyConfig> selectSysProxyConfigList(SysProxyConfig proxyConfig);

    SysProxyConfig selectSysProxyConfigById(Long sysProxyConfigId);

    int insertSysProxyConfig(SysProxyConfig sysProxyConfig);

    int updateSysProxyConfig(SysProxyConfig sysProxyConfig);

    void deleteSysProxyConfigById(Long sysProxyConfigId);

    void deleteSysProxyConfigByIds(Long[] sysProxyConfigIds);
}
