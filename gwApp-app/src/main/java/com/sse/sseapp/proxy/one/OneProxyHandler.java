package com.sse.sseapp.proxy.one;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.proxy.BaseProxyHandler;
import com.sse.sseapp.proxy.ProxyConfig;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/29 9:12
 */
@Component("one")
public class OneProxyHandler extends BaseProxyHandler {
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
        log.info("Proxy param           : {}", toJson(params));
        String result = HttpRequest.post(url)
                .header("Authorization", config.getBase().getAccessToken())
                .body(JSONUtil.toJsonStr(params))
                .timeout(50000)
                .execute()
                .body();
        log.info("Proxy result          : {}", result);
        return toBean(result, type);
    }
}
