package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.form.request.GetDueDateReqBody;
import com.sse.sseapp.form.request.GetSchoolsRateReqBody;
import com.sse.sseapp.form.request.OptionSchoolReqBody;
import com.sse.sseapp.form.request.SetSchoolsRateReqBody;
import com.sse.sseapp.form.response.SchoolsRateResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author : liuxinyu
 * @date : 2023/4/21 10:27
 */
@Service
@Slf4j
public class OptionSchoolService {

    @Autowired
    private ProxyProvider proxyProvider;
    @Autowired
    private RedisService redisService;


    /**
     * 正确返回码
     */
    private final String[] code = {"ACK", "0000"};


    @Value("${app.optionSchool.errCount}")
    private Integer errCount;

    public JSONObject common(BaseRequest<OptionSchoolReqBody> reqMap) {
        //跳出循环标志
        boolean flag = false;

        //重试次数
        Integer countFlag = 0;

        //期权学院
        Map<String, Object> data = BeanUtil.beanToMap(reqMap.getReqContent());
        String accessToken = reqMap.getBase().getAccessToken();
        Map<String, Object> result = null;

        //转发有可能报错，重试三次以后如果还错则直接报错
        while (countFlag < errCount && !flag) {
            try {
                result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_OPTION_SCHOOL_COMMON, data, mergeBaseVO(reqMap.getBase()), new TypeReference<Map<String, Object>>() {
                });
                flag = true;
            } catch (Exception e) {
                countFlag++;
                log.info("期权学院通用入口报错入参为: {},重试第" + countFlag + "次", reqMap.getReqContent());
            }
        }

        //如果result为空直接返回错误
        if (ObjUtil.isEmpty(result)) {
            throw new AppException("请稍后再试");
        }
        JSONObject jsonObject = JSONUtil.parseObj(result);

        //判断返回的code是否是正确返回码
        for (String s : code) {
            if (ObjUtil.equals(jsonObject.get("code").toString().toUpperCase(), s)) {
                return jsonObject;
            }
        }
        //如果是appToken过期的情况下
        if (ObjUtil.equals("401", StrUtil.toString(jsonObject.get("code")))) {
            //如果redis中存在appToken
            if (ObjUtil.isNotEmpty(redisService.getCacheObject(accessToken))) {
                redisService.deleteObject(accessToken);
            }
            //再次执行一次获取数据的操作
            for (int i = 0; i < 3; i++) {
                redisService.deleteObject(reqMap.getBase().getAccessToken());
                result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_OPTION_SCHOOL_COMMON, data, mergeBaseVO(reqMap.getBase()), new TypeReference<Map<String, Object>>() {
                });
                jsonObject = JSONUtil.parseObj(result);
                //判断返回的code是否是正确返回码
                for (String s : code) {
                    if (ObjUtil.equals(jsonObject.get("code").toString().toUpperCase(), s)) {
                        return jsonObject;
                    }
                }
                //flag标记次数标志增加
                countFlag++;
            }


            //重试三次以后    直接报错
            if (ObjUtil.equals(countFlag, errCount)) {
                throw new AppException("获取失败，请稍后再试");
            }
        }
        //如果不是登录过期的错误，则报错
        throw new AppException(StrUtil.toString(jsonObject.get("message")));
    }

    /**
     * 期权学苑登录
     *
     * @param reqBaseVO 请求头
     * @return 添加登录信息后请求头
     */
    private ReqBaseVO mergeBaseVO(ReqBaseVO reqBaseVO) {
        String accessToken = reqBaseVO.getAccessToken();
        if (StrUtil.isEmpty(accessToken)) {
            return reqBaseVO;
        }
        if (ObjUtil.isEmpty(redisService.getCacheObject(accessToken))) {
            //如果redis中没有token
            OptionSchoolReqBody reqBody = new OptionSchoolReqBody();
            Map<String, Object> params = new HashMap<>();
            params.put("accessToken", accessToken);
            params.put("version", "");
            params.put("packageType", "");
            reqBody.setUrl("tokenLogin");
            reqBody.setSendType("get");
            reqBody.setParams(params);
            Map<String, Object> data = BeanUtil.beanToMap(reqBody);
            Map<String, Object> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_OPTION_SCHOOL_COMMON, data, reqBaseVO, new TypeReference<Map<String, Object>>() {
            });
            JSONObject jsonObject = JSONUtil.parseObj(result);
            //判断返回的code是否是正确返回码
            for (String s : code) {
                if (ObjUtil.equals(jsonObject.get("code").toString().toUpperCase(), s)) {
                    //throw new AppException(StrUtil.toString(jsonObject.get("message")));
                    String appToken = JSONUtil.parseObj(jsonObject.get("data")).get("token").toString();
                    reqBaseVO.setAppToken(appToken);
                    //在redis中放置token    过期时间为两小时
                    redisService.setCacheObject(accessToken, StrUtil.toString(appToken), 7200L, TimeUnit.SECONDS);
                    return reqBaseVO;
                }
            }
            throw new AppException(ObjectUtil.isEmpty(jsonObject.get("message")) ? "请联系后台管理人员" : jsonObject.get("message").toString());
        } else {
            //如果redis中有token的话
            reqBaseVO.setAppToken(StrUtil.toString(redisService.getCacheObject(accessToken)));
            return reqBaseVO;
        }
    }

    public SchoolsRateResBody getSchoolsRate(BaseRequest<GetSchoolsRateReqBody> body) {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceid", body.getBase().getDeviceId());
        map.put("version", "2.3.6");
        SchoolsRateResBody result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_SCHOOL_RATE, map, null, new TypeReference<SchoolsRateResBody>() {
        });
        if (!Objects.equals("1", result.getStatus())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    public SchoolsRateResBody setSchoolsRate(BaseRequest<SetSchoolsRateReqBody> body) {
        Map<String, Object> map = BeanUtil.beanToMap(body.getReqContent());
        map.put("deviceid", body.getBase().getDeviceId());
        SchoolsRateResBody result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SET_SCHOOL_RATE, map, null, new TypeReference<SchoolsRateResBody>() {
        });
        if (!Objects.equals("1", result.getStatus())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }


    public JSONObject getDueDate(BaseRequest<GetDueDateReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());

        Map<String, Object> result = proxyProvider.proxy(ApiCodeConstants.SYS_COMMONQUERY, data, null, new TypeReference<Map<String, Object>>() {
        });

        //如果result为空直接返回错误
        if (ObjUtil.isEmpty(result)) {
            throw new AppException("请稍后再试");
        }
        JSONObject jsonObject = JSONUtil.parseObj(result);
        JSONObject json = new JSONObject();
        json.set("status", 1);
        json.set("msg", "success");
        json.set("VIXlist", jsonObject.get("result"));
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.set("code", "ACK");
        jsonObject1.set("message", "成功");
        jsonObject1.set("data", json);
        return jsonObject1;


    }


}
