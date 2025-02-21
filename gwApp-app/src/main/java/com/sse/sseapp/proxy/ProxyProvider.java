package com.sse.sseapp.proxy;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.domain.system.SysProxyConfig;
import com.sse.sseapp.feign.system.ISysProxyFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

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
    @Autowired
    ISysProxyFeign sysProxyFeign;

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

    public <T> T proxy(String configCode, Map<String, Object> data, ReqBaseVO base, TypeReference<T> type) {
        SysProxyConfig info = sysProxyFeign.getInfo(configCode);
        ProxyConfig proxyConfig = new ProxyConfig();
        BeanUtil.copyProperties(info, proxyConfig);
        proxyConfig.setData(data);
        proxyConfig.setBase(base);
        BaseProxyHandler handler = proxyHandlerMap.get(proxyConfig.getProxyType());
        if (handler == null) {
            throw new RuntimeException("第三方类型【" + proxyConfig.getProxyType() + "】暂不支持");
        }

        if (mockInterceptor == null) {
            return handler.proxy(proxyConfig, type);
        }
        T mock = mockInterceptor.mock(proxyConfig, type);
        if (mock != null) {
            return mock;
        }
        return handler.proxy(proxyConfig, type);
    }
}
