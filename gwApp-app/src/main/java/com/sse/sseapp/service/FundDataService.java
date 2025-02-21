package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.query.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FundDataService {
    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    CommonService commonService;

    public QueryResponse<RecentlyReleasedResBody> recentlyReleased(BaseRequest<RecentlyReleasedReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        QueryResponse<RecentlyReleasedResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, body.getBase(), new TypeReference<QueryResponse<RecentlyReleasedResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }

    public QueryResponse<RecentlyLaunchedResBody> recentlyLaunched(BaseRequest<RecentlyLaunchedReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        QueryResponse<RecentlyLaunchedResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, body.getBase(), new TypeReference<QueryResponse<RecentlyLaunchedResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }

    public QueryResponse<MarketOverviewResBody> marketOverview(BaseRequest<MarketOverviewReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        QueryResponse<MarketOverviewResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, body.getBase(), new TypeReference<QueryResponse<MarketOverviewResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }

    public QueryResponse<GetTypeResBody> getType(BaseRequest<GetTypeReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        QueryResponse<GetTypeResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, body.getBase(), new TypeReference<QueryResponse<GetTypeResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }

    public QueryResponse<FundQueryResBody> query(BaseRequest<FundQueryReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        QueryResponse<FundQueryResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, body.getBase(), new TypeReference<QueryResponse<FundQueryResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }

    public QueryResponse<DailyStatisticsResBody> dailyStatistics(BaseRequest<DailyStatisticsReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        QueryResponse<DailyStatisticsResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_COMMONQUERY, data, body.getBase(), new TypeReference<QueryResponse<DailyStatisticsResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }
}
