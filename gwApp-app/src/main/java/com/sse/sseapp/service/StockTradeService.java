package com.sse.sseapp.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.form.request.KcbJygkListReqBody;
import com.sse.sseapp.form.request.KcbZlpsListReqBody;
import com.sse.sseapp.form.request.MarketDataReqBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class StockTradeService {

    @Autowired
    private ProxyProvider proxyProvider;


    public RespBean<?> getMarketData(BaseRequest<MarketDataReqBody> reqBody) {
        String productType = reqBody.getReqContent().getProductType();
        if (ObjectUtil.isEmpty(productType)) {
            throw new AppException("参数校验失败");
        }
        Map<String, Object> data = BeanUtil.beanToMap(reqBody.getReqContent());
        SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_MARKET_DATA_OVERVIEW, data, reqBody.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("data",result);
        return RespBean.success(response);
    }

    public RespBean<?> getKcbZlpsList(BaseRequest<KcbZlpsListReqBody> reqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(reqBody.getReqContent());
        SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_KSB_ZL_PS_LIST, data, reqBody.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("data",result);
        return RespBean.success(response);
    }

    public RespBean<?> getKcbJygkList(BaseRequest<KcbJygkListReqBody> reqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(reqBody.getReqContent());
        SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_KSB_JY_GK_LIST, data, reqBody.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("data",result);
        return RespBean.success(response);
    }
}
