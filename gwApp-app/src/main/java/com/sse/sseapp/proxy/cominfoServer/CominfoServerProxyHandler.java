package com.sse.sseapp.proxy.cominfoServer;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.proxy.BaseProxyHandler;
import com.sse.sseapp.proxy.ProxyConfig;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/16 14:36 hanjian 创建
 */
@Component("cominfoServer")
public class CominfoServerProxyHandler extends BaseProxyHandler {
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
        HttpResponse result;
        if(ApiCodeConstants.SYS_PROXY_CODE_AUTH_IMAGE_VERIFICATION_CODE.equals(config.getCode())) {
            log.info("==============================>验证图形验证码请求设备号 {}   ,请求sessionId为 {}  ,请求code为 {}  ",
                    config.getBase().getDeviceId(),
                    config.getData().get("session"),
                    config.getData().get("data"));
            String cookie = params.remove("session") + "";
            result = new HttpRequest(UrlBuilder.of(url))
                    .method(Method.valueOf(config.getMethod().toUpperCase()))
                    .header("Cookie", cookie)
                    .disableCache()
                    .form(params)
                    .timeout(config.getRequestTimeout())
                    .execute();
        } else {
            result = new HttpRequest(UrlBuilder.of(url))
                    .method(Method.valueOf(config.getMethod().toUpperCase()))
                    .form(params)
                    .disableCache()
                    .timeout(config.getRequestTimeout())
                    .execute();
        }
        String body = result.body();
        if(ApiCodeConstants.SYS_PROXY_CODE_GET_IMAGE_VERIFICATION_CODE.equals(config.getCode())) {

            String cookieHeader = result.header("Set-Cookie");
            JSONObject jsonBody = new JSONObject();
            jsonBody.set("picCaptcha",body.replaceAll("\n", ""));
            if(StrUtil.isNotBlank(cookieHeader)) {
                int index = cookieHeader.indexOf("JSESSIONID");
                String sessionStr = cookieHeader.substring(index, index + 43);
                jsonBody.set("session",sessionStr);
            }
            jsonBody.set("status",result.getStatus()==200?"1":"0");
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>获取图形验证码请求设备号为 {}   ,请求sessionId为 {}  ",
                    config.getBase().getDeviceId(),
                    jsonBody.get("session"));
            body = jsonBody.toString();
        }
        log.info("Proxy result          : {}", body);
        return toBean(body, type);
    }
}
