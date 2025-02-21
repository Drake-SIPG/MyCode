package com.sse.sseapp.proxy.ipo;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.proxy.BaseProxyHandler;
import com.sse.sseapp.proxy.ProxyConfig;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/16 14:36 hanjian 创建
 */
@Component("ipo")
public class IpoProxyHandler extends BaseProxyHandler {
    @Override
    public <T> T execute(ProxyConfig config, TypeReference<T> type) {
        return execute0(config, type);
    }

    private <T> T execute0(ProxyConfig config, TypeReference<T> type) {
        String url = config.getUrlPrefix() + config.getProxyUri();
        log.info("Proxy url             : {}", url);
        log.info("Proxy method          : {}", config.getMethod());
        Map<String, Object> params = config.getData();
        log.info("Proxy param           : {}", toJson(params));
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
