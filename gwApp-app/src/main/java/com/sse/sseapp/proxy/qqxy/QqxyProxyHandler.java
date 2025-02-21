package com.sse.sseapp.proxy.qqxy;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.proxy.BaseProxyHandler;
import com.sse.sseapp.proxy.ProxyConfig;
import com.sse.sseapp.proxy.RetryException;
import com.sse.sseapp.proxy.cominfo.shaded.ScodeUtil;
import com.sse.sseapp.proxy.qqxy.shaded.AESOperator;
import com.sse.sseapp.proxy.qqxy.shaded.EncryUtil;
import com.sse.sseapp.proxy.qqxy.shaded.OptionSchoolScodeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cn.hutool.core.util.URLUtil.decode;
import static cn.hutool.json.JSONUtil.isJson;
import static cn.hutool.json.JSONUtil.parseObj;
import static com.google.common.collect.ImmutableMap.of;
import static com.sse.sseapp.proxy.CacheValueType.scode;
import static com.sse.sseapp.proxy.qqxy.shaded.MD5EncodeUtil.getUniqueId;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/16 14:36 hanjian 创建
 */
@Component("qqxy")
public class QqxyProxyHandler extends BaseProxyHandler {

    @Override
    public <T> T execute(ProxyConfig config, TypeReference<T> type) {
        String scode = getScode();
        log.info("Proxy scode           : {}", scode);
        return execute0(config, scode, type);
    }

    private <T> T execute0(ProxyConfig config, String scode, TypeReference<T> type) {
        String sendType = StrUtil.toString(config.getData().get("sendType"));
        String content = encrypt(config, scode);
        String url = config.getUrlPrefix() + config.getProxyUri();
        String method = StrUtil.isNullOrUndefined(sendType) ? config.getMethod() : sendType;
        if (StrUtil.equals(method.toUpperCase(), "POST")) {
            url = url + "?data=" + content;
        }

        log.info("Proxy url             : {}", url);
        log.info("Proxy method          : {}", method);
        Map<String, Object> params = of("data", content);

        Map<String, String> heads = new HashMap<>();
        heads.put("Content-Type", "application/json");
        heads.put("from", StrUtil.nullToDefault(config.getBase().getOSType(), "Android").toUpperCase().contains("ANDROID") ? "android" : "iOS");
        heads.put("X-Requested-With", "XMLHttpRequest");
        heads.put("app_version", "2.3.6");
        heads.put("mobileCode", getUniqueId(config.getBase().getDeviceId()));
        heads.put("Authorization", StrUtil.isNullOrUndefined(config.getBase().getAppToken()) ? config.getBase().getAccessToken() : config.getBase().getAppToken());

        log.info("Proxy header          : {}", heads);
        String result = new HttpRequest(UrlBuilder.of(url))
                .method(Method.valueOf(method.toUpperCase()))
                .form(params)
                .headerMap(heads, true)
                .timeout(config.getRequestTimeout())
                .execute()
                .body();
        log.info("Proxy result          : {}", result);
        if (isJson(result)) {
            @SuppressWarnings("VulnerableCodeUsages")
            JSONObject obj = parseObj(result);
            String code = obj.getStr("code");
            if (Objects.equals("40001", code)) {
                getCache(getKey(), OptionSchoolScodeUtil::getScode, true);
                throw new RetryException("scode错误，重新获取并重试");
            }

            return toBean(result, type);
        }

        String data = decode(decrypt(result, scode));
        log.info("Proxy result decrypted: {}", data);
        return toBean(data, type);
    }

    private String decrypt(String data, String scode) {
        return AESOperator.getInstance().decrypt(data, scode);
    }

    private String encrypt(ProxyConfig config, String scode) {
        Map<String, Object> data = config.getData();
        log.info("Proxy param           : {}", toJson(data));
        String url = data.get("url").toString();
        Object params = config.getData().get("params");
        @SuppressWarnings("unchecked")
        String content = EncryUtil.getInstance().EncryReqContent(url, params, scode);
        log.info("Proxy param encrypted : {}", content);
        return content;
    }

    private String getScode() {
        return getCache(getKey(), OptionSchoolScodeUtil::getScode, false);
    }

    private String getKey() {
        return AppConstants.QQXY + ":" + scode.name();
    }
}
