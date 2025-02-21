package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cmcc.idmp.rhsdk.gm.AllSMUtils;
import com.cmcc.idmp.rhsdk.gm.PrivateKey;
import com.cmcc.rhsdk.util.EncryptionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.core.utils.JsonUtil;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.cominfo.CominfoResponse;
import com.sse.sseapp.proxy.cominfo.dto.LoginDto;
import com.sse.sseapp.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static cn.hutool.http.Method.POST;

@Service
@Slf4j
public class OauthService {

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    AppMessageDeviceService appMessageDeviceService;

    @Autowired
    RedisService redisService;

    @Autowired
    ISysConfigFeign sysConfigFeign;

    String APPSecret;
    String oneLoginAppId;

    static String privateKeyBase64 = "GHbpVD3uy7KvDyQ51neV64efwkNkwguh53EOJjI8HAs=";
    private static String oneLoginSuccess = "103000";

    /**
     * 用户名密码登录
     */
    public CominfoResponse<LoginDto> login(BaseRequest<LoginReqBody> loginReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(loginReqBody.getReqContent());
        CominfoResponse<LoginDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_LOGIN, data, loginReqBody.getBase(), new TypeReference<CominfoResponse<LoginDto>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        //用户消息设备绑定
        appMessageDeviceService.userBindingMessageDevice(loginReqBody.getBase().getDeviceId(), result.getData().getPassId(), result.getData().getMobile());
        return result;
    }

    /**
     * 用户名密码登录
     */
    public CominfoResponse<LoginDto> loginNew(BaseRequest<LoginReqBody> loginReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(loginReqBody.getReqContent());
        CominfoResponse<LoginDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_LOGIN_NEW, data, loginReqBody.getBase(), new TypeReference<CominfoResponse<LoginDto>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        //用户消息设备绑定
        appMessageDeviceService.userBindingMessageDevice(loginReqBody.getBase().getDeviceId(), result.getData().getPassId(), result.getData().getMobile());
        return result;
    }

    /**
     * 短信登录
     */
    public CominfoResponse<SmsLoginResBody> smsLogin(BaseRequest<SmsLoginReqBody> smsLoginReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(smsLoginReqBody.getReqContent());
        CominfoResponse<SmsLoginResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SMS_LOGIN, data, smsLoginReqBody.getBase(), new TypeReference<CominfoResponse<SmsLoginResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        //用户消息设备绑定
        appMessageDeviceService.userBindingMessageDevice(smsLoginReqBody.getBase().getDeviceId(), result.getData().getPassId(), result.getData().getMobile());
        return result;
    }

    /**
     * 短信登录xc
     */
    public CominfoResponse<SmsLoginResBody> smsLoginNew(BaseRequest<SmsLoginReqBody> smsLoginReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(smsLoginReqBody.getReqContent());
        CominfoResponse<SmsLoginResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SMS_LOGIN_NEW, data, smsLoginReqBody.getBase(), new TypeReference<CominfoResponse<SmsLoginResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        //用户消息设备绑定
        appMessageDeviceService.userBindingMessageDevice(smsLoginReqBody.getBase().getDeviceId(), result.getData().getPassId(), result.getData().getMobile());
        return result;
    }

    /**
     * 忘记密码
     */
    public CominfoResponse<ForgotPasswordResBody> forgotPassword(BaseRequest<ForgotPasswordReqBody> forgotPasswordReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(forgotPasswordReqBody.getReqContent());
        CominfoResponse<ForgotPasswordResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_FORGOT_PASSWORD, data, forgotPasswordReqBody.getBase(), new TypeReference<CominfoResponse<ForgotPasswordResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 忘记密码
     */
    public CominfoResponse<ForgotPasswordResBody> forgotPasswordNew(BaseRequest<ForgotPasswordReqBody> forgotPasswordReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(forgotPasswordReqBody.getReqContent());
        CominfoResponse<ForgotPasswordResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_FORGOT_PASSWORD_NEW, data, forgotPasswordReqBody.getBase(), new TypeReference<CominfoResponse<ForgotPasswordResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 注册
     */
    public CominfoResponse<RegisterResBody> register(BaseRequest<RegisterReqBody> registerReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(registerReqBody.getReqContent());
        CominfoResponse<RegisterResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_REGISTER, data, registerReqBody.getBase(), new TypeReference<CominfoResponse<RegisterResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 注册xc
     */
    public CominfoResponse<RegisterResBody> registerNew(BaseRequest<RegisterReqBody> registerReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(registerReqBody.getReqContent());
        CominfoResponse<RegisterResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_REGISTER_NEW, data, registerReqBody.getBase(), new TypeReference<CominfoResponse<RegisterResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 登出
     *
     * @param loginOutReqBody
     * @return
     */
    public CominfoResponse<LoginOutResBody> loginOut(BaseRequest<LoginOutReqBody> loginOutReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(loginOutReqBody.getReqContent());
        CominfoResponse<LoginOutResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_LOGIN_OUT, data, loginOutReqBody.getBase(), new TypeReference<CominfoResponse<LoginOutResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        //设备解绑
        appMessageDeviceService.userUnBindingMessageDevice(loginOutReqBody.getBase().getDeviceId());
        return result;
    }

    /**
     * 登出
     *
     * @param loginOutReqBody
     * @return
     */
    public CominfoResponse<LoginOutResBody> loginOutNew(BaseRequest<LoginOutReqBody> loginOutReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(loginOutReqBody.getReqContent());
        CominfoResponse<LoginOutResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_LOGIN_OUT_NEW, data, loginOutReqBody.getBase(), new TypeReference<CominfoResponse<LoginOutResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        //设备解绑
        appMessageDeviceService.userUnBindingMessageDevice(loginOutReqBody.getBase().getDeviceId());
        return result;
    }

    /**
     * 获取短信验证
     */
    public CominfoResponse<SmsCaptchaResBody> smsCaptcha(BaseRequest<SmsCaptchaReqBody> smsCaptchaReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(smsCaptchaReqBody.getReqContent());
        CominfoResponse<SmsCaptchaResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SMS_CAPTCHA, data, smsCaptchaReqBody.getBase(), new TypeReference<CominfoResponse<SmsCaptchaResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException("获取短信验证码失败");
        }
        return result;
    }

    /**
     * 获取短信验证
     */
    public CominfoResponse<SmsCaptchaResBody> smsCaptchaNew(BaseRequest<SmsCaptchaReqBody> smsCaptchaReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(smsCaptchaReqBody.getReqContent());
        CominfoResponse<SmsCaptchaResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SMS_CAPTCHA_NEW, data, smsCaptchaReqBody.getBase(), new TypeReference<CominfoResponse<SmsCaptchaResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException("获取短信验证码失败");
        }
        return result;
    }

    /**
     * 获取图形验证码
     */
    public CominfoResponse<PicCaptchaResBody> picCaptcha(BaseRequest<PicCaptchaReqBody> picCaptchaReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(picCaptchaReqBody.getReqContent());
        CominfoResponse<PicCaptchaResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_PIC_CAPTCHA, data, picCaptchaReqBody.getBase(), new TypeReference<CominfoResponse<PicCaptchaResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 获取图形验证码xc
     */
    public CominfoResponse<PicCaptchaResBody> picCaptchaNew(BaseRequest<PicCaptchaReqBody> picCaptchaReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(picCaptchaReqBody.getReqContent());
        CominfoResponse<PicCaptchaResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_PIC_CAPTCHA_NEW, data, picCaptchaReqBody.getBase(), new TypeReference<CominfoResponse<PicCaptchaResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 二维码扫描登录
     */
    public CominfoResponse<ScanLoginResBody> scanLogin(BaseRequest<ScanLoginReqBody> scanLoginReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(scanLoginReqBody.getReqContent());
        CominfoResponse<String> response = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SCAN_LOGIN, data, scanLoginReqBody.getBase(), new TypeReference<CominfoResponse<String>>() {
        });
        if (!Objects.equals(1, response.getState())) {
            throw new AppException(response.getMsg());
        }

        CominfoResponse<ScanLoginResBody> result = new CominfoResponse<>();
        result.setData(new ScanLoginResBody());
        result.setState(response.getState());
        result.setMsg(response.getMsg());

        return result;
    }

    /**
     * 二维码扫描登录xc
     */
    public CominfoResponse<ScanLoginResBody> scanLoginNew(BaseRequest<ScanLoginReqBody> scanLoginReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(scanLoginReqBody.getReqContent());
        CominfoResponse<String> response = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SCAN_LOGIN_NEW, data, scanLoginReqBody.getBase(), new TypeReference<CominfoResponse<String>>() {
        });
        if (!Objects.equals(1, response.getState())) {
            throw new AppException(response.getMsg());
        }

        CominfoResponse<ScanLoginResBody> result = new CominfoResponse<>();
        result.setData(new ScanLoginResBody());
        result.setState(response.getState());
        result.setMsg(response.getMsg());

        return result;
    }

    /**
     * 信创获取scancode
     */
    public CominfoResponse<GetScanCodeResBody> getScanCode(BaseRequest<GetScanCodeReqBody> scanLoginReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(scanLoginReqBody.getReqContent());
        CominfoResponse<GetScanCodeResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_SCAN_CODE, data, scanLoginReqBody.getBase(), new TypeReference<CominfoResponse<GetScanCodeResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 信创获取scancode
     */
    public CominfoResponse<GetClientIdResBody> getClientId(BaseRequest<GetScanCodeReqBody> scanLoginReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(scanLoginReqBody.getReqContent());
        CominfoResponse<GetClientIdResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_CLIENT_ID, data, scanLoginReqBody.getBase(), new TypeReference<CominfoResponse<GetClientIdResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 查看头像
     */
    public CominfoResponse<GetUserImageResBody> getUserImage(BaseRequest<GetUserImageReqBody> getUserImageReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(getUserImageReqBody.getReqContent());
        CominfoResponse<String> response = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_USER_IMAGE, data, getUserImageReqBody.getBase(), new TypeReference<CominfoResponse<String>>() {
        });
        //往返回体中设置初始值
        CominfoResponse<GetUserImageResBody> result = new CominfoResponse<>();
        if (!Objects.equals(1, response.getState())) {
            result.setMsg("获取头像失败");
            return result;
        }
        String encode = Base64.encode(response.getData().getBytes(StandardCharsets.ISO_8859_1));
        response.setData(encode);

        result.setData(new GetUserImageResBody());
        result.setState(response.getState());
        result.getData().setImg(response.getData());
        result.setMsg(response.getMsg());
        return result;
    }

    /**
     * 查看头像xc
     */
    public CominfoResponse<GetUserImageResBody> getUserImageNew(BaseRequest<GetUserImageReqBody> getUserImageReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(getUserImageReqBody.getReqContent());
        CominfoResponse<String> response = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_USER_IMAGE_NEW, data, getUserImageReqBody.getBase(), new TypeReference<CominfoResponse<String>>() {
        });
        //往返回体中设置初始值
        CominfoResponse<GetUserImageResBody> result = new CominfoResponse<>();
        if (!Objects.equals(1, response.getState())) {
            result.setMsg("获取头像失败");
            return result;
        }
        String encode = Base64.encode(response.getData().getBytes(StandardCharsets.ISO_8859_1));
        response.setData(encode);

        result.setData(new GetUserImageResBody());
        result.setState(response.getState());
        result.getData().setImg(response.getData());
        result.setMsg(response.getMsg());
        return result;
    }

    public CominfoResponse<RefreshTokenResBody> refreshToken(BaseRequest<RefreshTokenReqBody> refreshTokenReqBody) {
        CominfoResponse<RefreshTokenResBody> result = new CominfoResponse<>();
        String uid = refreshTokenReqBody.getBase().getUid();

        boolean lock = redisService.redisTemplate.opsForValue().setIfAbsent("refreshToken_lock:" + uid, "刷新token", 5, TimeUnit.SECONDS);
        if (lock) {
            try {
                //先判断缓存是否存在
                String redisKey = "refreshToken:" + uid;
                CominfoResponse<RefreshTokenResBody> redisValue = redisService.getCacheObject(redisKey);
                if (ObjectUtil.isNotEmpty(redisValue)) {
                    return redisValue;
                }

                //请求接口
                Map<String, Object> data = BeanUtil.beanToMap(refreshTokenReqBody.getReqContent());
                data.put("accessToken", refreshTokenReqBody.getBase().getAccessToken());
                CominfoResponse<String> response = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_REFRESH_TOKEN, data, refreshTokenReqBody.getBase(), new TypeReference<CominfoResponse<String>>() {
                });
                if (!Objects.equals(1, response.getState())) {
                    throw new AppException(response.getMsg());
                }

                result.setData(new RefreshTokenResBody());
                result.setState(response.getState());
                result.getData().setAccessToken(response.getData());
                result.setMsg(response.getMsg());
                //存入缓存
                redisService.setCacheObject(redisKey, result, 5L, TimeUnit.SECONDS);
            } finally {
                //释放锁
                redisService.redisTemplate.delete("refreshToken_lock:" + uid);
            }
        } else {
            //加锁失败重新调用
            refreshToken(refreshTokenReqBody);
        }
        return result;
    }

    public CominfoResponse<RefreshTokenResBody> refreshTokenNew(BaseRequest<RefreshTokenReqBody> refreshTokenReqBody) {
        CominfoResponse<RefreshTokenResBody> result = new CominfoResponse<>();
        String uid = refreshTokenReqBody.getBase().getUid();

        boolean lock = redisService.redisTemplate.opsForValue().setIfAbsent("refreshToken_lock_xc:" + uid, "信创刷新token", 5, TimeUnit.SECONDS);
        if (lock) {
            try {
                //先判断缓存是否存在
                String redisKey = "refreshToken:" + uid;
                CominfoResponse<RefreshTokenResBody> redisValue = redisService.getCacheObject(redisKey);
                if (ObjectUtil.isNotEmpty(redisValue)) {
                    return redisValue;
                }

                //请求接口
                Map<String, Object> data = BeanUtil.beanToMap(refreshTokenReqBody.getReqContent());
                data.put("accessToken", refreshTokenReqBody.getBase().getAccessToken());
                CominfoResponse<String> response = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_REFRESH_TOKEN_NEW, data, refreshTokenReqBody.getBase(), new TypeReference<CominfoResponse<String>>() {
                });
                if (!Objects.equals(1, response.getState())) {
                    throw new AppException(response.getMsg());
                }

                result.setData(new RefreshTokenResBody());
                result.setState(response.getState());
                result.getData().setAccessToken(response.getData());
                result.setMsg(response.getMsg());
                //存入缓存
                redisService.setCacheObject(redisKey, result, 5L, TimeUnit.SECONDS);
            } finally {
                //释放锁
                redisService.redisTemplate.delete("refreshToken_lock:" + uid);
            }
        } else {
            //加锁失败重新调用
            refreshToken(refreshTokenReqBody);
        }
        return result;
    }


    /**
     * 一键登录
     */
    public CominfoResponse<LoginDto> oneClickLogin(BaseRequest<OneClickLoginReqBody> oneClickLoginReqBody) {

        Map<String, Object> data = BeanUtil.beanToMap(oneClickLoginReqBody.getReqContent());
        if (oneClickLoginReqBody.getBase().getOSType().toLowerCase().contains("ios")) {
            APPSecret = this.sysConfigFeign.getConfigKey("APPSecretiOS");
            oneLoginAppId = this.sysConfigFeign.getConfigKey("oneLoginAppIdiOS");
        } else {
            APPSecret = this.sysConfigFeign.getConfigKey("APPSecretAndroid");
            oneLoginAppId = this.sysConfigFeign.getConfigKey("oneLoginAppIdAndroid");

        }
        Map<String, Object> params = new HashMap<>();
        params.put("appid", oneLoginAppId);
        log.info("移动一键登录appid——————————>" + oneClickLoginReqBody.getReqContent().getAppid());
        params.put("version", "3.5");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String currentTime = LocalDateTime.now().format(formatter);
        params.put("systemtime", currentTime);
        Random random = new Random();
        int randomNumberOfDigits = random.nextInt(36) + 1;
        // 使用StringBuilder来构建随机数字字符串
        StringBuilder randomNumberBuilder = new StringBuilder();
        for (int i = 0; i < randomNumberOfDigits; i++) {
            randomNumberBuilder.append(random.nextInt(10));
        }
        params.put("msgid", randomNumberBuilder.toString());
        params.put("strictcheck", "1");
        params.put("token", oneClickLoginReqBody.getReqContent().getToken());
        String s = oneLoginAppId + "3.5" + randomNumberBuilder.toString() + currentTime + "1" + oneClickLoginReqBody.getReqContent().getToken() + APPSecret;
        try {
            byte[] cipherBytes = AllSMUtils.sm2Sign2(privateKeyBase64, s.getBytes(StandardCharsets.UTF_8));
            String sign = java.util.Base64.getEncoder().encodeToString(cipherBytes);
            params.put("sign", sign);
        } catch (Exception e) {
            log.info("移动一键登录异常——————————>国密加密异常");
            throw new AppException("移动一键登录异常");
        }
        params.put("encryptionalgorithm", "SM");
        try {
            String host = this.sysConfigFeign.getConfigKey(AppConstants.J_PUSH_PROXY_HOST);
            String port = this.sysConfigFeign.getConfigKey(AppConstants.J_PUSH_PROXY_PORT);
            String oneLoginUrl = this.sysConfigFeign.getConfigKey("oneLoginUrl");
            String body = JSON.toJSONString(params);
            String result = new HttpRequest(UrlBuilder.of(oneLoginUrl))
                    .method(Method.valueOf("POST"))
                    .body(body)
                    .setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, NumberUtil.parseInt(port))))
                    .execute()
                    .body();
            log.info("移动一键登录返回——————————>" + result);
            JSONObject jsonObject = JSON.parseObject(result);
            if (!Objects.equals(oneLoginSuccess, jsonObject.get("resultCode"))) {
                log.info("移动一键登录异常——————————>" + jsonObject);
                throw new AppException(jsonObject.getString("desc"));
            } else {
                byte[] msisdnCipherBytes = java.util.Base64.getDecoder().decode(jsonObject.getString("msisdn"));
                // 最后一个参数兼容其他厂商的密文格式。一般使用false就行
                byte[] msisdnBytes = AllSMUtils.sm2Dec2(privateKeyBase64, msisdnCipherBytes, false);
                // 明文手机号
                String msisdn = new String(msisdnBytes, StandardCharsets.UTF_8);
                data.put("loginName", msisdn);
            }
        } catch (Exception e) {
            log.info("移动一键登录异常——————————>" + e);
            throw new AppException("移动一键登录异常");

        }

        CominfoResponse<LoginDto> response = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_ONE_CLICK_LOGIN, data, oneClickLoginReqBody.getBase(), new TypeReference<CominfoResponse<LoginDto>>() {
        });
        if (!Objects.equals(1, response.getState())) {
            throw new AppException(response.getMsg());
        }

        appMessageDeviceService.userBindingMessageDevice(oneClickLoginReqBody.getBase().getDeviceId(), response.getData().getPassId(), response.getData().getMobile());

        return response;
    }

}
