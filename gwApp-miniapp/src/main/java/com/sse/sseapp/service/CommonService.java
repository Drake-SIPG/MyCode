package com.sse.sseapp.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.utils.Sm4Util;
import com.sse.sseapp.core.constant.CacheConstants;
import com.sse.sseapp.core.text.Convert;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.feign.system.ISysProxyFeign;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 公共方法
 *
 * @author zhengyaosheng
 * @date 2023-03-17
 */
@Service
public class CommonService {
    @Autowired
    ProxyProvider proxyProvider;
    @Autowired
    ISysProxyFeign sysProxyFeign;
    @Autowired
    private ISysConfigFeign sysConfigFeign;
    @Autowired
    private RedisService redisService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MiniAppService miniAppService;

    /**
     * 获取key
     *
     * @param defaultKey 取默认加解密key
     * @return 加解密key
     */
    public String getKey(boolean defaultKey) {
        if (defaultKey) {
            String key = sysConfigFeign.getConfigKey(AppConstants.ENCRYPT_KEY);
            if (CharSequenceUtil.isBlank(key)) {
                throw new AppException("默认加密key未配置");
            }
            return key;
        }

        //上锁
        Boolean success = redisTemplate.opsForValue().setIfAbsent("generateScode", "生成scode", 5, TimeUnit.SECONDS);
        if (success != null && success) {
            try {
                String key = Convert.toStr(redisService.getCacheObject(getCacheKey()));
                if (StringUtils.isNotEmpty(key)) {
                    return key;
                }
                key = Sm4Util.generateKey();
                String timeout = sysConfigFeign.getConfigKey(AppConstants.ENCRYPT_KEY_TIMEOUT);
                if (StringUtils.isEmpty(timeout)) {
                    timeout = AppConstants.DEFAULT_ENCRYPT_KEY_TIMEOUT;
                }
                redisService.setCacheObject(getCacheKey(), key, Long.parseLong(timeout), TimeUnit.MINUTES);
                return key;
            } finally {
                //解锁
                redisTemplate.delete("generateScode");
            }
        } else {
            //加锁失败重新调用
            return getKey(false);
        }
    }


    /**
     * 设置cache key
     *
     * @return 缓存键key
     */
    private String getCacheKey() {
        return CacheConstants.SYS_CONFIG_KEY + AppConstants.INBORN_ENCRYPT_KEY;
    }

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        miniAppService.checkStatus();
    }
}
