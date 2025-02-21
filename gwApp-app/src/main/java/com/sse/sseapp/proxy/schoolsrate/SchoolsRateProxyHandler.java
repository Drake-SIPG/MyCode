package com.sse.sseapp.proxy.schoolsrate;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.proxy.BaseProxyHandler;
import com.sse.sseapp.proxy.ProxyConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Component("schoolsRate")
public class SchoolsRateProxyHandler extends BaseProxyHandler {

    /**
     * hutool加密
     */
    private static String key;

    @Value("${key.encodeKey}")
    public String encodeKey;

    @PostConstruct
    private void setKey() {
        key = this.encodeKey;
    }

    @Override
    public <T> T execute(ProxyConfig config, TypeReference<T> type) {
        return execute0(config, type);
    }

    public static String encrypt(String content, String key) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            keyGen.generateKey().getEncoded();
            Key seckey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, seckey);
            byte[] result = cipher.doFinal(content.getBytes());
            return Base64.encode(result);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String stringToJsonComm(Map<String, Object> hashMap) {
        String reString;
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> stringObjectEntry : hashMap.entrySet()) {
            String key = (String) ((Map.Entry) stringObjectEntry).getKey();
            String value = (String) stringObjectEntry.getValue();
            try {
                if (key != null && value != null) {
                    jsonObject.put(key, value);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        reString = jsonObject.toString();
        return reString;
    }

    private <T> T execute0(ProxyConfig config, TypeReference<T> type) {
        String param = stringToJsonComm(config.getData());
        String content = encrypt(param, key);
        String url = config.getUrlPrefix() + config.getProxyUri();
        log.info("Proxy url             : {}", url);
        log.info("Proxy method          : {}", config.getMethod());
        String result = HttpRequest.of(url)
                .method(Method.valueOf(config.getMethod().toUpperCase()))
                .body(content)
                .clearHeaders()
                .header("Content-Type", "text/plain; charset=utf-8")
                .timeout(config.getRequestTimeout())
                .execute()
                .body();
        log.info("Proxy result          : {}", result);
        return toBean(result, type);
    }

}
