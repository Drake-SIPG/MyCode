package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author : liuxinyu
 * @date : 2023/5/24 14:45
 */
@Service
public class MonthReportService {
    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    ISysConfigFeign sysConfigFeign;

    String key;

    public Object getDataList(BaseRequest<GetDataListReqBody> getDataList) {
        //判断必填参数是否必填
        if (ObjectUtil.isEmpty(getDataList.getReqContent().getParams()) || ObjectUtil.isEmpty(getDataList.getReqContent().getUrl())) {
            throw new AppException("传入参数为空");
        }
        String url = getDataList.getReqContent().getUrl();


        int index = 0;
        for (int i = 0; i < 3; i++) {
            index = url.indexOf("/", index) + 1;
        }
        if (index > 0) {
            key = url.substring(index);
        } else {
            throw new AppException("url异常");
        }

        String queryList = sysConfigFeign.getConfigKey("queryList");
        String queryHost = sysConfigFeign.getConfigKey("queryHost");
        String[] queryListArr = queryList.split("\\,");

        if (Arrays.asList(queryListArr).contains(key)) {
            url = queryHost + key;
        } else {
            throw new AppException("url异常");
        }

        //根据分隔符分别获取参数并存放到map中
        String[] paramsArr = getDataList.getReqContent().getParams().split("\\|");
        Map<String, Object> params = new HashMap<>();
        for (String paramsArrStr : paramsArr) {
            String[] paramsArrStrs = paramsArrStr.split("=");
            if (paramsArrStrs.length == 1) {
                if (!paramsArrStr.startsWith("=")) {
                    params.put(paramsArrStrs[0], "");
                }
            } else if (paramsArrStrs.length == 2) {
                params.put(paramsArrStrs[0], paramsArrStrs[1]);
            }
        }
        String body = HttpRequest.post(url)
                .header("Referer", "http://www.sse.com.cn")
                .method(Method.POST)
                .form(params)
                .timeout(50000)
                .execute()
                .body();
        Map<String, Object> response = new HashMap<>();
        response.put("data", getList(body));
        return response;
    }

    public SoaResponse<GetListedCompanyAreaResBody> getListedCompanyArea(BaseRequest<GetListedCompanyAreaReqBody> getListedCompanyArea) {
        Map<String, Object> data = BeanUtil.beanToMap(getListedCompanyArea.getReqContent());
        SoaResponse<GetListedCompanyAreaResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_LISTED_COMPANY_AREA, data, getListedCompanyArea.getBase(), new TypeReference<SoaResponse<GetListedCompanyAreaResBody>>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }


    public SoaResponse<GetListedCompanyOverViewResBody> getListedCompanyOverView(BaseRequest<GetListedCompanyOverViewReqBody> getListedCompanyArea) {
        Map<String, Object> data = BeanUtil.beanToMap(getListedCompanyArea.getReqContent());
        SoaResponse<GetListedCompanyOverViewResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_LISTED_COMPANY_OVER_VIEW, data, getListedCompanyArea.getBase(), new TypeReference<SoaResponse<GetListedCompanyOverViewResBody>>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    public SoaResponse<GetMemberAreaResBody> getMemberArea(BaseRequest<GetMemberAreaReqBody> getMbmberAreaReq) {
        Map<String, Object> data = BeanUtil.beanToMap(getMbmberAreaReq.getReqContent());
        SoaResponse<GetMemberAreaResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_MEMBER_AREA, data, getMbmberAreaReq.getBase(), new TypeReference<SoaResponse<GetMemberAreaResBody>>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    public SoaResponse<GetMemberTradeResBody> getMemberTrade(BaseRequest<GetMemberTradeReqBody> getMemberAreaReq) {
        Map<String, Object> data = BeanUtil.beanToMap(getMemberAreaReq.getReqContent());
        SoaResponse<GetMemberTradeResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_MEMBER_TRADE, data, getMemberAreaReq.getBase(), new TypeReference<SoaResponse<GetMemberTradeResBody>>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    public SoaResponse<GetBranchTradeResBody> getBranchTrade(BaseRequest<GetBranchTradeReqBody> getBranchTrade) {
        Map<String, Object> data = BeanUtil.beanToMap(getBranchTrade.getReqContent());
        SoaResponse<GetBranchTradeResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_BRANCH_TRADE, data, getBranchTrade.getBase(), new TypeReference<SoaResponse<GetBranchTradeResBody>>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    public SoaResponse<GetRzrqTradeResBody> getRzrqTrade(BaseRequest<GetRzrqTradeReqBody> getRzrqTrade) {
        Map<String, Object> data = BeanUtil.beanToMap(getRzrqTrade.getReqContent());
        SoaResponse<GetRzrqTradeResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_RZRQ_TRADE, data, getRzrqTrade.getBase(), new TypeReference<SoaResponse<GetRzrqTradeResBody>>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    public List<JSONObject> getList(String content) {

        List<JSONObject> list = new ArrayList<>();
        content = content.substring(content.indexOf("{"), content.lastIndexOf("}") + 1);
        JSONObject jsonObject = JSONUtil.parseObj(content);

        if (ObjectUtil.isNotNull(jsonObject.get("result"))) {
            JSONArray array = jsonObject.getJSONArray("result");

            Iterator<Object> it = array.iterator();
            JSONObject temp;
            while (it.hasNext()) {
                temp = JSONUtil.parseObj(it.next());
                list.add(temp);
            }
        }
        return list;
    }
}
