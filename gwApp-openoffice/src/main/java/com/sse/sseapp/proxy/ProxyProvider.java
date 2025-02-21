package com.sse.sseapp.proxy;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/16 14:58 hanjian 创建
 */
@Component
public class ProxyProvider {
    @Autowired
    private Map<String, BaseProxyHandler> proxyHandlerMap;
    @Autowired(required = false)
    private MockInterceptor mockInterceptor;

    public <T> T proxy(ProxyConfig config, TypeReference<T> type) {
        String proxyType = config.getProxyType();
        BaseProxyHandler handler = proxyHandlerMap.get(proxyType);
        if (handler == null) {
            throw new RuntimeException("第三方类型【" + proxyType + "】暂不支持");
        }

        if (mockInterceptor == null) {
            return handler.proxy(config, type);
        }
        T mock = mockInterceptor.mock(config, type);
        if (mock != null) {
            return mock;
        }
        return handler.proxy(config, type);
    }
}
