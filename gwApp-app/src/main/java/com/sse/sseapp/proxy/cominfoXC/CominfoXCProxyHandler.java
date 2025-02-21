package com.sse.sseapp.proxy.cominfoXC;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.base.RespEnum;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.exception.ProxyException;
import com.sse.sseapp.proxy.BaseProxyHandler;
import com.sse.sseapp.proxy.ProxyConfig;
import com.sse.sseapp.proxy.RetryException;
import com.sse.sseapp.proxy.cominfo.shaded.*;
import com.sse.sseapp.service.AppMessageDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static cn.hutool.core.bean.BeanUtil.copyProperties;
import static cn.hutool.core.text.CharSequenceUtil.isBlank;
import static cn.hutool.json.JSONUtil.parseObj;
import static com.google.common.collect.ImmutableMap.of;
import static com.sse.sseapp.proxy.CacheValueType.scode;
import static com.sse.sseapp.proxy.cominfo.shaded.AESOperator.getInstance;
import static com.sse.sseapp.proxy.cominfo.shaded.ScodeUtil.decodeScode;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/16 14:36 hanjian 创建
 */
@Component("cominfo-xc")
public class CominfoXCProxyHandler extends BaseProxyHandler {

    @Autowired
    private AppMessageDeviceService appMessageDeviceService;

    @Override
    public <T> T execute(ProxyConfig config, TypeReference<T> type) {
        String scode = getScode();
        log.info("Proxy scode           : {}", scode);
        return execute0(config, scode, type);
    }

    private <T> T execute0(ProxyConfig config, String scode, TypeReference<T> type) {
        String content = encrypt(config, scode);
        String url = config.getUrlPrefix() + config.getProxyUri();
        log.info("Proxy url             : {}", url);
        log.info("Proxy method          : {}", config.getMethod());
        Map<String, Object> params = of("data", content);
        String result = HttpRequest.of(url)
                .method(Method.valueOf(config.getMethod().toUpperCase()))
                .form(params)
                .timeout(config.getRequestTimeout())
                .execute()
                .body();
        log.info("Proxy result          : {}", result);

        result = filter(result);
        JSONObject obj = parseObj(result);
        String state = obj.getStr("state");
        String data = obj.getStr("data");
        String msg = obj.getStr("msg");

        if (Objects.equals("0", state) && Objects.equals("用户未登录", msg)) {
            throw new ProxyException(RespEnum.NO_LOGIN, msg);
        }

        //设备id
        String deviceId = config.getBase().getDeviceId();
        switch (state) {
            case "1":
                return toBean(result, type);
            case "222222":
                String json = decrypt(data, scode);
                log.info("Proxy result decrypted: {}", json);
                json = filter(json);
                return toBean(json, type);
            case "100001":
                getCache(getKey(), () -> decodeScode(data), true);
                throw new RetryException("scode错误，重新获取并重试");
            case "10005":
            case "10006":
            case "10007":
                //设备解绑
                appMessageDeviceService.userUnBindingMessageDevice(deviceId);
                //登录信息失效
                throw new ProxyException(RespEnum.LOGIN_INFO_INVALID, msg);
            case "10004":
                //token已过期
                throw new ProxyException(RespEnum.ACCESSTOKEN_OVERDUW, RespEnum.ACCESSTOKEN_OVERDUW.getMessage());
            case "100007":
            case "10001":
            case "10002":
                //设备解绑
                appMessageDeviceService.userUnBindingMessageDevice(deviceId);
                // token检验失败
                throw new ProxyException(RespEnum.TOKEN_INVALID, RespEnum.TOKEN_INVALID.getMessage());
            default:
                break;
        }


        return toBean(result, type);
    }

    private String filter(String json) {
        //处理不规范的data数据
        JSONObject obj = parseObj(json);
        String data = obj.getStr("data");
        if (isBlank(data)) {
            obj.set("data", null);
        }
        return obj.toString();
    }

    private String decrypt(String data, String scode) {
        return getInstance().decrypt(data, scode);
    }

    private String encrypt(ProxyConfig config, String scode) {
        BaseDTO base = buildBase(config);
        if (config.encrypt()) {
            CominfoRequest cominfoRequest = new CominfoRequest();
            cominfoRequest.getBase().setAppBundle(base.getAppBundle());
            cominfoRequest.getBase().getDataParam().setBase(base);
            cominfoRequest.getBase().getDataParam().setReqContent(config.getData());
            String json = toJson(cominfoRequest.getBase().getDataParam());
            log.info("Proxy param           : {}", json);
            json = getInstance().encrypt(json, scode);
            log.info("Proxy param encrypted : {}", json);
            CominfoEncryptedSendRequest cominfoEncryptedSendRequest = new CominfoEncryptedSendRequest();
            cominfoEncryptedSendRequest.getBase().setAppBundle(base.getAppBundle());
            cominfoEncryptedSendRequest.getBase().setDataParam(json);
            return toJson(cominfoEncryptedSendRequest);
        }
        CominfoSendRequest dataParamDTO = new CominfoSendRequest();
        dataParamDTO.setBase(base);
        dataParamDTO.setReqContent(config.getData());
        String content = toJson(dataParamDTO);
        log.info("Proxy param           : {}", content);
        return content;
    }

    private BaseDTO buildBase(ProxyConfig config) {
        BaseDTO base = new BaseDTO();
        ReqBaseVO reqBaseVO = config.getBase();
        if (reqBaseVO == null) {
            return base;
        }
        copyProperties(reqBaseVO, base, CopyOptions.create().ignoreNullValue());
        return base;
    }

    private String getScode() {
        return getCache(getKey(), ScodeUtil::getScodeXC, false);
    }

    private String getKey() {
        return AppConstants.COMINFO + ":" + scode.name();
    }
}
