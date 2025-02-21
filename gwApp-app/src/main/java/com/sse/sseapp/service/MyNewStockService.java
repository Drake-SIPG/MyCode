package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.GetPhDataByUserReqBody;
import com.sse.sseapp.form.request.GetZqDataByUserReqBody;
import com.sse.sseapp.form.response.GetPhDataByUserResBody;
import com.sse.sseapp.form.response.GetZqDataByUserResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : liuxinyu
 * @date : 2023/4/10 9:54
 */
@Service
public class MyNewStockService {
    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    CommonService commonService;

    @Autowired
    ISysConfigFeign sysConfigFeign;

    /**
     * 注意：老代码中input标签中还有msgId这个标签，但是没有默认值，在请求体中未定义，老的接口文档中也没有看见请求体加了msgId这个键值。
     * 但是在SOA接口说明文档(0.4)中提到：msgId用于日志查询和记录，请传入uuid
     */
    public SoaResponse<GetPhDataByUserResBody> getPhDataByUser(BaseRequest<GetPhDataByUserReqBody> getPhDataByUserReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(getPhDataByUserReqBody.getReqContent());
        //检查用户登录状态
        commonService.cominfoCheck(getPhDataByUserReqBody.getBase());
        //放置token
        data.put("token", getPhDataByUserReqBody.getBase().getAccessToken());
        data.put("client_id", this.sysConfigFeign.getConfigKey(AppConstants.CLIENT_ID));
        SoaResponse<GetPhDataByUserResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_PH_DATA_BY_USER, data, null, new TypeReference<SoaResponse<GetPhDataByUserResBody>>() {
        });
        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            return result;
        }
        //转换日期格式
        for (GetPhDataByUserResBody list : result.getList()) {
            list.setPh_date(dateConversion(list.getPh_date()));
        }
        return result;
    }

    public SoaResponse<GetZqDataByUserResBody> getZqDataByUser(BaseRequest<GetZqDataByUserReqBody> getZqDataByUserReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(getZqDataByUserReqBody.getReqContent());
        //检查用户登录状态
        commonService.cominfoCheck(getZqDataByUserReqBody.getBase());
        //放置token及client_id
        data.put("token", getZqDataByUserReqBody.getBase().getAccessToken());
        data.put("client_id", this.sysConfigFeign.getConfigKey(AppConstants.CLIENT_ID));
        SoaResponse<GetZqDataByUserResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_ZQ_DATA_BY_USER, data, null, new TypeReference<SoaResponse<GetZqDataByUserResBody>>() {
        });
        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            return result;
        }
        //转换日期格式
        for (GetZqDataByUserResBody list : result.getList()) {
            list.setZq_date(dateConversion(list.getZq_date()));
        }
        return result;
    }

    /**
     * 将yyyyMMdd的格式转为yyyy-MM-dd
     */
    private static String dateConversion(String date) {
        return DateUtil.parse(date).toString(DatePattern.NORM_DATE_PATTERN);
    }
}
