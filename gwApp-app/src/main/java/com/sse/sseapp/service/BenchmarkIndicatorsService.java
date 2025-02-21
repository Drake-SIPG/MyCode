package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.IndexTrendReqBody;
import com.sse.sseapp.form.request.YieldCurveReqBody;
import com.sse.sseapp.form.response.IndexTrendInfoResBody;
import com.sse.sseapp.form.response.IndexTrendResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.query.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 基准指标
 *
 * @author wy
 * @date 2023-08-09
 */
@Service
public class BenchmarkIndicatorsService {

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    ISysConfigFeign sysConfigFeign;

    /**
     * 指数走势概况
     */
    public RespBean<?> indexTrendInfo(BaseRequest<IndexTrendReqBody> baseRequest) {
        try {
            String url = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
            url += baseRequest.getReqContent().getMarket();
            url += "/snap/";
            url += baseRequest.getReqContent().getDebtType();
            String result = HttpRequest.get(url).header("Referer", "http://www.sse.com.cn").execute().body();
            IndexTrendInfoResBody indexTrendInfoResBody = JSONUtil.toBean(result, IndexTrendInfoResBody.class);
            if (ObjectUtil.isNotEmpty(indexTrendInfoResBody.getSnap())) {
                Map<String, String> map = new HashMap<>(3);
                map.put("last", indexTrendInfoResBody.getSnap().get(5).toString());
                map.put("change", indexTrendInfoResBody.getSnap().get(6).toString());
                map.put("chgRate", indexTrendInfoResBody.getSnap().get(7).toString());
                return RespBean.success(map);
            } else {
                throw new AppException("调用三方接口异常，请联系管理员");
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    /**
     * 指数走势
     */
    public RespBean<?> indexTrend(BaseRequest<IndexTrendReqBody> baseRequest) {
        try {
            String url = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
            url += baseRequest.getReqContent().getMarket();
            url += "/line/";
            url += baseRequest.getReqContent().getDebtType();
            url += "?begin=" + baseRequest.getReqContent().getBegin();
            url += "&end=" + baseRequest.getReqContent().getEnd();
            url += "&select=time,price,volume";
            String result = HttpRequest.get(url).header("Referer", "http://www.sse.com.cn").execute().body();
            if (JSONUtil.isJson(result)) {
                IndexTrendResBody indexTrendResBody = JSONUtil.toBean(result, IndexTrendResBody.class);
                return RespBean.success(indexTrendResBody);
            } else {
                throw new AppException("调用三方接口异常，请联系管理员");
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    /**
     * 收益率曲线
     */
    public RespBean<?> yieldCurve(BaseRequest<YieldCurveReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        QueryResponse result = proxyProvider.proxy(ApiCodeConstants.SYS_COMMONQUERY, data, baseRequest.getBase(), new TypeReference<QueryResponse>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return RespBean.success(result);
    }
}
