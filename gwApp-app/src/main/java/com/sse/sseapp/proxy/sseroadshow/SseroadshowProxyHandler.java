package com.sse.sseapp.proxy.sseroadshow;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.SSEConstant;
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
@Component("sseroadshow")
public class SseroadshowProxyHandler extends BaseProxyHandler {
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
        String result;
        if(params.containsKey(SSEConstant.Authorization)){
            result = new HttpRequest(UrlBuilder.of(url))
                    .method(Method.valueOf(config.getMethod().toUpperCase()))
                    .timeout(config.getRequestTimeout())
                    .disableCache()
                    .disableCookie()
                    .header(SSEConstant.Authorization,"Bearer " + params.get(SSEConstant.Authorization))
                    .form(params)
                    .execute()
                    .body();
        } else {
            result = new HttpRequest(UrlBuilder.of(url))
                    .method(Method.valueOf(config.getMethod().toUpperCase()))
                    .timeout(config.getRequestTimeout())
                    .disableCache()
                    .disableCookie()
                    .form(params)
                    .execute()
                    .body();
        }
        log.info("Proxy result          : {}", result);
        return toBean(result, type);
    }
}
