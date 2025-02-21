package com.sse.sseapp.proxy.sso2;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.proxy.BaseProxyHandler;
import com.sse.sseapp.proxy.ProxyConfig;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("SSO2")
public class SSO2ProxyHandler extends BaseProxyHandler {

    @Override
    public <T> T execute(ProxyConfig config, TypeReference<T> type) {
        T data = execute0(config, type);
        return data;
    }

    private <T> T execute0(ProxyConfig config, TypeReference<T> type) {
        String url = config.getUrlPrefix() + config.getProxyUri();
        log.info("Proxy url             : {}", url);
        log.info("Proxy method          : {}", config.getMethod());
        Map<String, Object> params = config.getData();
        String result = new HttpRequest(url)
                .method(Method.valueOf(config.getMethod().toUpperCase()))
                .form(params)
                .timeout(config.getRequestTimeout())
                .execute()
                .body();
        log.info("Proxy result          : {}", result);
        return toBean(result, type);
    }
}
