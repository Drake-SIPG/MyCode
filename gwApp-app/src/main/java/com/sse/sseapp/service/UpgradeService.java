package com.sse.sseapp.service;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.form.request.VersionUpgradeNewReqBody;
import com.sse.sseapp.form.request.VersionUpgradeReqBody;
import com.sse.sseapp.form.response.NavResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.cominfo.CominfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UpgradeService {

    @Autowired
    private ProxyProvider proxyProvider;

    public RespBean<?> versionUpgrade(BaseRequest<VersionUpgradeReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        Map<String, Object> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_VERSION_UPGRADE, data, baseRequest.getBase(), new TypeReference<Map<String, Object>>() {
        });
        return RespBean.success(result);
    }

    public RespBean<?> versionUpgradeNew(BaseRequest<VersionUpgradeNewReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        Map<String, Object> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_VERSION_UPGRADE_NEW, data, baseRequest.getBase(), new TypeReference<Map<String, Object>>() {
        });
        /*CominfoResponse<NavResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_VERSION_UPGRADE_NEW, data, baseRequest.getBase(), new TypeReference<CominfoResponse<NavResBody>>() {
        });*/
        return RespBean.success(result);
    }
}
