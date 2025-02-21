package com.sse.sseapp.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.GetDerivativeNoticeListResBody;
import com.sse.sseapp.form.response.SupervisionListResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.mysoa.MySoaResponse;
import com.sse.sseapp.proxy.soa.SoaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class NoticeService {

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    private CommonService commonService;

    @Autowired
    ISysConfigFeign sysConfigFeign;
    String key;

    public RespBean<?> setBulletinReadCount(BaseRequest<BulletinReadCountReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (ObjectUtil.isEmpty(data.get("docId"))) {
            throw new AppException("参数校验失败");
        }
        data.put("deviceId", baseRequest.getBase().getDeviceId());
        data.put("deviceType", baseRequest.getBase().getDeviceType());
        data.put("userId", baseRequest.getBase().getUid());
        data.put("version", baseRequest.getBase().getAppVersion());
        MySoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_SET_BULLETIN_READ_COUNT, data, baseRequest.getBase(), new TypeReference<MySoaResponse<Map<String, Object>>>() {
        });
        if (ObjectUtil.notEqual(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return RespBean.success(result);

    }

    public JSONArray baseNoticeList(BaseRequest<BaseNoticeListReqBody> baseRequest) {
        BaseNoticeListReqBody reqContent = baseRequest.getReqContent();
        String paramsStr = reqContent.getParams();
        String url = reqContent.getUrl();
        if (StrUtil.isEmpty(paramsStr) || StrUtil.isEmpty(url)) {
            throw new AppException("params或url未传参");
        }

//        query.sse.com.cn/soaAction.do
//            query.sse.com.cn/commonSoaQuery.do
//            query.sse.com.cn/security/stock/queryCompanyBulletinNew.do
//            query.sse.com.cn/commonQuery.do

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


        String[] paramsArr = paramsStr.split("\\;");
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
        String post = HttpRequest.post(url)
                .header("Referer", "http://www.sse.com.cn")
                .method(Method.POST)
                .form(params)
                .timeout(50000)
                .execute()
                .body();
        log.info("上证基金/债券公告转发接口请求三方接口返回值为:{}", post);
        JSONArray array = new JSONArray();
        JSONObject json = JSONUtil.parseObj(post);
        if (ObjectUtil.isNotEmpty(json.get("result"))) {
            array = json.getJSONArray("result");
        }
        return array;
    }

    public RespBean supervisionListCMS(BaseRequest<SupervisionListReqBody> baseRequest) {
        Map<String, String> map = commonService.getActive("supervisionList");
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (ObjectUtil.isNotEmpty(baseRequest.getReqContent().getType())) {
            data.put("channelId", map.get(baseRequest.getReqContent().getType()));
        } else {
            data.put("channelId", map.get("default"));
        }
        data.put("stockcode", data.get("code"));
        data.remove("code");
        SoaResponse<SupervisionListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST_CMS, data, baseRequest.getBase(), new TypeReference<SoaResponse<SupervisionListResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return RespBean.success(result);
    }

    public List<GetDerivativeNoticeListResBody> derivativeNoticeList(BaseRequest<DerivativeNoticeListReqBody> baseRequest) {
        DerivativeNoticeListReqBody reqContent = baseRequest.getReqContent();
        Map<String, Object> param = BeanUtil.beanToMap(reqContent);
        String pageSize = reqContent.getPageSize();
        Integer pageSizeIn;
        if (StrUtil.isEmpty(pageSize)) {
            pageSizeIn = 20;
        } else {
            pageSizeIn = Integer.parseInt(pageSize);
        }
        Map<String, String> map = commonService.getActive("derivativeNoticeList");
        if (ObjectUtil.isNotEmpty(baseRequest.getReqContent().getType())) {
            param.put("channelId", map.get(baseRequest.getReqContent().getType()));
        } else {
            param.put("channelId", param.get("default"));
        }
        param.put("docTitle", reqContent.getTitle());
        param.remove("title");
        param.put("pageSize", pageSizeIn);
        SoaResponse<GetDerivativeNoticeListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_GET_YSP_NOTICE_LIST, param, baseRequest.getBase(), new TypeReference<SoaResponse<GetDerivativeNoticeListResBody>>() {
        });
        if (!StrUtil.equals(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException("请求返回结果失败，请稍后再试");
        }
        List<GetDerivativeNoticeListResBody> resultList = null;
        if (ObjectUtil.isNotEmpty(result.getList())) {
            resultList = result.getList();
        }
        return resultList;
    }

    public List<GetDerivativeNoticeListResBody> derivativeNoticeListCMS(BaseRequest<DerivativeNoticeListReqBody> baseRequest) {
        DerivativeNoticeListReqBody reqContent = baseRequest.getReqContent();
        Map<String, Object> param = BeanUtil.beanToMap(reqContent);
        String pageSize = reqContent.getPageSize();
        Integer pageSizeIn;
        if (StrUtil.isEmpty(pageSize)) {
            pageSizeIn = 20;
        } else {
            pageSizeIn = Integer.parseInt(pageSize);
        }
        Map<String, String> map = commonService.getActive("derivativeNoticeList");
        if (ObjectUtil.isNotEmpty(baseRequest.getReqContent().getType())) {
            param.put("channelId", map.get(baseRequest.getReqContent().getType()));
        } else {
            param.put("channelId", param.get("default"));
        }
        param.put("docTitle", reqContent.getTitle());
        param.remove("title");
        param.put("pageSize", pageSizeIn);
        SoaResponse<GetDerivativeNoticeListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST_CMS, param, baseRequest.getBase(), new TypeReference<SoaResponse<GetDerivativeNoticeListResBody>>() {
        });
        if (!StrUtil.equals(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException("请求返回结果失败，请稍后再试");
        }
        List<GetDerivativeNoticeListResBody> resultList = null;
        if (ObjectUtil.isNotEmpty(result.getList())) {
            resultList = result.getList();
        }
        return resultList;
    }


    public RespBean supervisionList(BaseRequest<SupervisionListReqBody> baseRequest) {
        Map<String, String> map = commonService.getActive("supervisionList");
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (ObjectUtil.isNotEmpty(baseRequest.getReqContent().getType())) {
            data.put("channelId", map.get(baseRequest.getReqContent().getType()));
        } else {
            data.put("channelId", map.get("default"));
        }
        data.put("stockcode", data.get("code"));
        data.remove("code");
        SoaResponse<SupervisionListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST, data, baseRequest.getBase(), new TypeReference<SoaResponse<SupervisionListResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return RespBean.success(result);
    }

    public List<GetDerivativeNoticeListResBody> pactNoticeList(BaseRequest<PactNoticeListReqBody> baseRequest) {
        PactNoticeListReqBody reqContent = baseRequest.getReqContent();
        Map<String, Object> param = BeanUtil.beanToMap(reqContent);
        String pageSize = reqContent.getPageSize();
        Integer pageSizeIn;
        if (StrUtil.isEmpty(pageSize)) {
            pageSizeIn = 20;
        } else {
            pageSizeIn = Integer.parseInt(pageSize);
        }
        Map<String, String> map = commonService.getActive("pactNoticeList");
        if (ObjectUtil.isNotEmpty(baseRequest.getReqContent().getType())) {
            param.put("channelId", map.get(baseRequest.getReqContent().getType()));
        } else {
            param.put("channelId", param.get("default"));
        }
        param.put("docTitle", reqContent.getTitle());
        param.remove("title");
        param.put("pageSize", pageSizeIn);
        SoaResponse<GetDerivativeNoticeListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_GET_YSP_NOTICE_LIST, param, baseRequest.getBase(), new TypeReference<SoaResponse<GetDerivativeNoticeListResBody>>() {
        });
        if (!StrUtil.equals(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException("请求返回结果失败，请稍后再试");
        }
        List<GetDerivativeNoticeListResBody> resultList = null;
        if (ObjectUtil.isNotEmpty(result.getList())) {
            resultList = result.getList();
        }
        return resultList;
    }


    public List<GetDerivativeNoticeListResBody> pactNoticeListCMS(BaseRequest<PactNoticeListReqBody> baseRequest) {
        PactNoticeListReqBody reqContent = baseRequest.getReqContent();
        Map<String, Object> param = BeanUtil.beanToMap(reqContent);
        String pageSize = reqContent.getPageSize();
        Integer pageSizeIn;
        if (StrUtil.isEmpty(pageSize)) {
            pageSizeIn = 20;
        } else {
            pageSizeIn = Integer.parseInt(pageSize);
        }
        Map<String, String> map = commonService.getActive("pactNoticeList");
        if (ObjectUtil.isNotEmpty(baseRequest.getReqContent().getType())) {
            param.put("channelId", map.get(baseRequest.getReqContent().getType()));
        } else {
            param.put("channelId", param.get("default"));
        }
        param.put("docTitle", reqContent.getTitle());
        param.remove("title");
        param.put("pageSize", pageSizeIn);
        SoaResponse<GetDerivativeNoticeListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST_CMS, param, baseRequest.getBase(), new TypeReference<SoaResponse<GetDerivativeNoticeListResBody>>() {
        });
        if (!StrUtil.equals(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException("请求返回结果失败，请稍后再试");
        }
        List<GetDerivativeNoticeListResBody> resultList = null;
        if (ObjectUtil.isNotEmpty(result.getList())) {
            resultList = result.getList();
        }
        return resultList;
    }
}
