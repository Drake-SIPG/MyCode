package com.sse.sseapp.proxy.query;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.base.RespEnum;
import com.sse.sseapp.app.core.exception.ProxyException;
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
@Component("query")
public class QueryProxyHandler extends BaseProxyHandler {
    @Override
    public <T> T execute(ProxyConfig config, TypeReference<T> type) {
        T data = execute0(config, type);
        return data;
    }

    private <T> T execute0(ProxyConfig config, TypeReference<T> type) {
        try {
            String url = config.getUrlPrefix() + config.getProxyUri();
            log.info("Proxy url             : {}", url);
            log.info("Proxy method          : {}", config.getMethod());
            Map<String, Object> params = config.getData();
            params.put("jsonCallBack", "callback");
            params.put("_", System.currentTimeMillis());
            log.info("Proxy param           : {}", toJson(params));
            String result = HttpRequest.of(url)
                    .method(Method.valueOf(config.getMethod().toUpperCase()))
                    .header("Referer", "https://www.sse.com.cn")
                    .form(params)
                    .timeout(config.getRequestTimeout())
                    .execute()
                    .body().trim();
            log.info("Proxy result          : {}", result);
            if (result.startsWith("callback")) {
                result = result.substring("callback(".length());
                result = result.substring(0, result.length() - 1);
            }
            return toBean(result, type);
        } catch (Exception e) {
            throw new ProxyException(RespEnum.ERROR, "网络较慢，请重新打开页面");
        }
    }
}
