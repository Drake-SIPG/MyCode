package com.sse.sseapp.service.push.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.core.constant.CacheConstants;
import com.sse.sseapp.core.constant.UserConstants;
import com.sse.sseapp.core.text.Convert;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.core.utils.ToolUtil;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.push.AppPushConfig;
import com.sse.sseapp.mapper.push.AppPushConfigMapper;
import com.sse.sseapp.redis.service.RedisService;
import com.sse.sseapp.service.push.AppPushConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import static com.sse.sseapp.core.web.domain.AjaxResult.success;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-06-07
 */
@Service
public class AppPushConfigServiceImpl extends ServiceImpl<AppPushConfigMapper, AppPushConfig> implements AppPushConfigService {

    @Autowired
    private AppPushConfigMapper appPushConfigMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public AppPushConfig selectConfigById(String configId) {
        return this.appPushConfigMapper.selectById(configId);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisService.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        LambdaQueryWrapper<AppPushConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AppPushConfig::getPushConfigKey, configKey);
        AppPushConfig retConfig = this.appPushConfigMapper.selectOne(lambdaQueryWrapper);
        if (StringUtils.isNotNull(retConfig)) {
            redisService.setCacheObject(getCacheKey(configKey), retConfig.getPushConfigValue());
            return retConfig.getPushConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 查询参数配置列表
     *
     * @param entity 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public Page<AppPushConfig> selectConfigList(AppPushConfig entity) {
        Page<AppPushConfig> page = new Page<>(entity.getCurrent(), entity.getPageSize());
        LambdaQueryWrapper<AppPushConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ToolUtil.isNotEmpty(entity.getPushConfigKey()), AppPushConfig::getPushConfigKey, entity.getPushConfigKey());
        lambdaQueryWrapper.like(ToolUtil.isNotEmpty(entity.getPushConfigName()), AppPushConfig::getPushConfigName, entity.getPushConfigName());
        lambdaQueryWrapper.eq(ToolUtil.isNotEmpty(entity.getPushConfigValue()), AppPushConfig::getPushConfigValue, entity.getPushConfigValue());
        Page<AppPushConfig> iPage = (Page<AppPushConfig>) this.page(page, lambdaQueryWrapper);
        return iPage;
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(AppPushConfig config) {
        return this.appPushConfigMapper.insert(config);
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(AppPushConfig config) {
        LambdaQueryWrapper<AppPushConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AppPushConfig::getId, config.getId());
        AppPushConfig temp = this.appPushConfigMapper.selectOne(lambdaQueryWrapper);
        if (!StringUtils.equals(temp.getPushConfigKey(), config.getPushConfigKey())) {
            redisService.deleteObject(getCacheKey(temp.getPushConfigKey()));
        }
        int row = appPushConfigMapper.updateById(config);
        if (row > 0) {
            redisService.setCacheObject(getCacheKey(config.getPushConfigKey()), config.getPushConfigValue());
        }
        return row;
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    public AjaxResult deleteConfigByIds(String[] configIds) {
        for (String configId : configIds) {
            AppPushConfig config = this.appPushConfigMapper.selectById(configId);
            this.appPushConfigMapper.deleteById(configId);
            redisService.deleteObject(getCacheKey(config.getPushConfigKey()));
        }
        return success();
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        List<AppPushConfig> configsList = this.appPushConfigMapper.selectList(new LambdaQueryWrapper<>());
        for (AppPushConfig config : configsList) {
            redisService.setCacheObject(getCacheKey(config.getPushConfigKey()), config.getPushConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        Collection<String> keys = redisService.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisService.deleteObject(keys);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(AppPushConfig config) {
        LambdaQueryWrapper<AppPushConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AppPushConfig::getPushConfigKey, config.getPushConfigKey());
        if (ObjectUtil.isNotEmpty(config.getId())) {
            lambdaQueryWrapper.ne(AppPushConfig::getId, config.getId());
        }
        AppPushConfig retConfig = this.appPushConfigMapper.selectOne(lambdaQueryWrapper);
        if (ObjectUtil.isNotEmpty(retConfig)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.APP_PUSH_CONFIG_KEY + configKey;
    }
}
