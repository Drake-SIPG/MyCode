package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.form.request.ChannelCategoryDataReqBody;
import com.sse.sseapp.form.response.ChannelCategoryDataResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * @author : liuxinyu
 * @date : 2023/5/31 10:14
 */
@Service
public class BridgingService {
    @Autowired
    ProxyProvider proxyProvider;

    public ChannelCategoryDataResBody getChannelCategoryData(BaseRequest<ChannelCategoryDataReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
//        List<Map<String, Object>> list = new ArrayList<>();
//        String[] types = body.getReqContent().getType().split("_");
//        for (int i = 0; i < types.length; i++) {
//            data.remove("type");
//            data.put("type", types[i]);
        SoaResponse<ChannelCategoryDataResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_CHANNEL_CATEGORY_DATA, data, body.getBase(), new TypeReference<SoaResponse<ChannelCategoryDataResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }

//            Map<String, Object> item = new HashMap<>();
//            item.put("data", result);
//            item.put("type", types[i]);
//            list.add(item);
//        }

        return result.getList().get(0);
    }
}
