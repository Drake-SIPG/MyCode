package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;

import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.query.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CodeDataService {
    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    ISysConfigFeign sysConfigFeign;

    public QueryResponse<DataOverviewResBody> dataOverview(BaseRequest<DataOverviewReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        QueryResponse<DataOverviewResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, body.getBase(), new TypeReference<QueryResponse<DataOverviewResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }

    public QueryResponse<DisclosureReportListResBody> disclosureReportList(BaseRequest<DisclosureReportListReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        QueryResponse<DisclosureReportListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, body.getBase(), new TypeReference<QueryResponse<DisclosureReportListResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }

    public QueryResponse<DailyFundOverviewResBody> dailyFundOverview(BaseRequest<DailyFundOverviewReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        QueryResponse<DailyFundOverviewResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, body.getBase(), new TypeReference<QueryResponse<DailyFundOverviewResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }

    public QueryResponse<ValueRankingResBody> valueRanking(BaseRequest<ValueRankingReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        data.put("pageHelp.pageSize", body.getReqContent().getPageSize());
        data.put("pageHelp.cacheSize", "1");
        QueryResponse<ValueRankingResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, body.getBase(), new TypeReference<QueryResponse<ValueRankingResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }

    public QueryResponse<ActivityRankingResBody> activityRanking(BaseRequest<ActivityRankingReqBody> body) {
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        data.put("pageHelp.pageSize", body.getReqContent().getPageSize());
        data.put("pageHelp.cacheSize", "1");
        QueryResponse<ActivityRankingResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, body.getBase(), new TypeReference<QueryResponse<ActivityRankingResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }
}
