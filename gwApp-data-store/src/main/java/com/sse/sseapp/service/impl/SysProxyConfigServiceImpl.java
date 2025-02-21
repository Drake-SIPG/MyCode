package com.sse.sseapp.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.core.annotation.DataScope;
import com.sse.sseapp.core.constant.CacheConstants;
import com.sse.sseapp.core.text.Convert;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.core.utils.uuid.SnowUtils;
import com.sse.sseapp.domain.system.SysConfig;
import com.sse.sseapp.domain.system.SysProxyConfig;
import com.sse.sseapp.mapper.system.SysProxyConfigMapper;
import com.sse.sseapp.redis.service.RedisService;
import com.sse.sseapp.service.system.SysProxyConfigService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 第三方接口配置表 服务实现类
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-27
 */
@Service
public class SysProxyConfigServiceImpl extends ServiceImpl<SysProxyConfigMapper, SysProxyConfig> implements SysProxyConfigService {

    @Autowired
    private SysProxyConfigMapper sysProxyConfigMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public SysProxyConfig selectConfigByCode(String configCode) {
        SysProxyConfig sysProxyConfig = redisService.getCacheObject(getCacheKey(configCode));
        if (ObjectUtil.isNotEmpty(sysProxyConfig)) {
            return sysProxyConfig;
        }
        SysProxyConfig sysProxyConfig1 = lambdaQuery().eq(SysProxyConfig::getCode, configCode).one();
        if (StringUtils.isNotNull(sysProxyConfig1)) {
            redisService.setCacheObject(getCacheKey(configCode), sysProxyConfig1);
            return sysProxyConfig1;
        }
        return null;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysProxyConfig> selectSysProxyConfigList(SysProxyConfig proxyConfig) {
        return sysProxyConfigMapper.selectSysProxyConfigList(proxyConfig);
    }

    @Override
    public SysProxyConfig selectSysProxyConfigById(Long sysProxyConfigId) {
        return sysProxyConfigMapper.selectSysProxyConfigById(sysProxyConfigId);
    }

    @Override
    public int insertSysProxyConfig(SysProxyConfig sysProxyConfig) {
        //设置唯一uuid
        sysProxyConfig.setId(SnowUtils.bigKey());
        int row = sysProxyConfigMapper.insertSysProxyConfig(sysProxyConfig);
        if (row > 0) {
            //写入缓存
            redisService.setCacheObject(getCacheKey(sysProxyConfig.getCode()), sysProxyConfig);
        }
        return row;
    }

    @Override
    public int updateSysProxyConfig(SysProxyConfig sysProxyConfig) {
        int row = sysProxyConfigMapper.updateSysProxyConfig(sysProxyConfig);
        if (row > 0) {
            //删除缓存
            redisService.deleteObject(getCacheKey(sysProxyConfig.getCode()));
            //更新缓存
            redisService.setCacheObject(getCacheKey(sysProxyConfig.getCode()), sysProxyConfig);
        }
        return row;
    }


    @Override
    public void deleteSysProxyConfigByIds(Long[] sysProxyConfigIds) {
        for (Long sysProxyConfigId : sysProxyConfigIds) {
            //根据id查询
            SysProxyConfig sysProxyConfig = sysProxyConfigMapper.selectSysProxyConfigById(sysProxyConfigId);
            //删除缓存
            redisService.deleteObject(getCacheKey(sysProxyConfig.getCode()));
            sysProxyConfigMapper.deleteSysProxyConfigById(sysProxyConfigId);
        }
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.APP_PROXY_CONFIG_KEY + configKey;
    }

    /**
     * 项目启动时，清除三方接口缓存
     */
    @PostConstruct
    @SneakyThrows
    public void init() {
        SysProxyConfig proxyConfig = new SysProxyConfig();
        List<SysProxyConfig> list = sysProxyConfigMapper.selectSysProxyConfigList(proxyConfig);
        for (SysProxyConfig sysProxyConfig : list) {
            redisService.deleteObject(getCacheKey(sysProxyConfig.getCode()));
        }
    }
}
