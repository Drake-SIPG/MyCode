package com.sse.sseapp.proxy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sse.sseapp.app.core.exception.AppException;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import static cn.hutool.core.text.CharSequenceUtil.isBlank;
import static cn.hutool.core.text.CharSequenceUtil.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/16 14:34 hanjian 创建
 */
public abstract class BaseProxyHandler {
    protected final Logger log = getLogger("ProxyHandler");
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Retryable(include = RetryException.class, backoff = @Backoff(value = 0))
    public <T> T proxy(ProxyConfig config, TypeReference<T> type) {
        log.info("Proxy type            : {}", config.getProxyType());
        log.info("Proxy input           : {}", config.getData());
        String configJson = toJson(config);
        log.info("Proxy config          : {}", configJson);
        return execute(config, type);
    }

    public abstract <T> T execute(ProxyConfig config, TypeReference<T> type);

    @SneakyThrows
    @Recover
    public <T> T recover(Exception ex, ProxyConfig config, TypeReference<T> type) {
        log.warn("Proxy error           : " + ex.getMessage(), ex);
        throw ex;
    }

    protected String getCache(String key, Supplier<String> init, boolean reload) {
        if (reload) {
            stringRedisTemplate.delete(key);
        }

        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String value = opsForValue.get(key);
        if (isNotBlank(value)) {
            return value;
        }

        String initValue = init.get();
        if (isBlank(initValue)) {
            throw new AppException("获取scode失败");
        }

        opsForValue.set(key, initValue);
        return initValue;
    }

    @SneakyThrows
    protected String toJson(Object value) {
        if (value instanceof String) {
            return (String) value;
        }
        return objectMapper.writeValueAsString(value);
    }

    @SneakyThrows
    protected <T> T toBean(String value, TypeReference<T> type) {
        return objectMapper.readValue(value, type);
    }
}
