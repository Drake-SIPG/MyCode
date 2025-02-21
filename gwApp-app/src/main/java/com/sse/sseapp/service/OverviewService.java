package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.feign.system.ISysProxyFeign;
import com.sse.sseapp.form.request.TurnoverReqBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.query.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 债券成交概览
 *
 * @author wy
 * @date 2023-08-09
 */
@Service
public class OverviewService {

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    ISysProxyFeign sysProxyFeign;

    /**
     * 收益率曲线
     */
    public RespBean<?> turnover(BaseRequest<TurnoverReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        QueryResponse result = proxyProvider.proxy(ApiCodeConstants.SYS_COMMONQUERY, data, baseRequest.getBase(), new TypeReference<QueryResponse>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return RespBean.success(result);
    }
}
