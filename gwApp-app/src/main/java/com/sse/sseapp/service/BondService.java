package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.app.AppBondSubscription;
import com.sse.sseapp.domain.app.AppBondSubscriptionPo;
import com.sse.sseapp.feign.app.IAppBondSubscriptionFeign;
import com.sse.sseapp.feign.system.ISysProxyFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.BondFundamentalsResBody;
import com.sse.sseapp.form.response.BusinessPromptResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.query.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BondService {
    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    CommonService commonService;

    @Autowired
    ISysProxyFeign sysProxyFeign;

    @Autowired
    IAppBondSubscriptionFeign bondSubscriptionFeign;

    public QueryResponse<BusinessPromptResBody> businessPrompt(BaseRequest<BusinessPromptReqBody> request) {
        BusinessPromptReqBody reqContent = request.getReqContent();
        reqContent.setActionType(commonService.getActive("businessActionType").get(reqContent.getActionType()));
        reqContent.setBulletinType(commonService.getActive("businessBulletinType").get(reqContent.getBulletinType()));
        Map<String, Object> data = BeanUtil.beanToMap(reqContent);
        data.remove("pageSize");
        data.remove("pageNo");
        data.put("pageHelp.beginPage", request.getReqContent().getPageNo());
        data.put("pageHelp.pageSize", request.getReqContent().getPageSize());
        data.put("pageHelp.cacheSize", 1);
        QueryResponse<BusinessPromptResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_COMMONQUERY, data, request.getBase(), new TypeReference<QueryResponse<BusinessPromptResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }

    public QueryResponse<BondFundamentalsResBody> bondFundamentals(BaseRequest<BondFundamentalsReqBody> request) {
        Map<String, Object> data = BeanUtil.beanToMap(request.getReqContent());
        QueryResponse<BondFundamentalsResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_COMMONQUERY, data, request.getBase(), new TypeReference<QueryResponse<BondFundamentalsResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return result;
    }

    public JSONArray getUserBondSubscription(BaseRequest<GetUserBondSubscriptionReqBody> request) {
        if (StrUtil.isEmpty(request.getBase().getUid())) {
            throw new AppException("用户ID未传");
        }
        List<String> userBondSubscriptions = bondSubscriptionFeign.getUserBondSubscription(request.getBase().getUid());
        if (ObjectUtil.isNotEmpty(request.getReqContent().getBondCode())) {
            userBondSubscriptions = userBondSubscriptions.stream().filter(a -> a.contains(request.getReqContent().getBondCode())).collect(Collectors.toList());
        }
        BusinessPromptReqBody businessPromptReqBody = new BusinessPromptReqBody();
        BeanUtil.copyProperties(request.getReqContent(), businessPromptReqBody);
        businessPromptReqBody.setActionType(commonService.getActive("businessActionType").get(request.getReqContent().getActionType()));
        businessPromptReqBody.setBulletinType(commonService.getActive("businessBulletinType").get(request.getReqContent().getBulletinType()));
        Map<String, Object> data = BeanUtil.beanToMap(businessPromptReqBody);
        data.put("pageHelp.pageSize", 1000);
        data.put("pageHelp.pageNo", 1);
        data.put("pageHelp.beginPage", 1);
        data.put("pageHelp.cacheSize", 5);
        QueryResponse<BusinessPromptResBody> allList = proxyProvider.proxy(ApiCodeConstants.SYS_COMMONQUERY, data, request.getBase(), new TypeReference<QueryResponse<BusinessPromptResBody>>() {
        });

        BaseRequest<BondFundamentalsReqBody> baseRequest = new BaseRequest<>();
        JSONArray response = new JSONArray();
        baseRequest.setBase(request.getBase());
        for (String userBondSubscription : userBondSubscriptions) {
            Optional<BusinessPromptResBody> businessPrompt = allList.getResult().stream().filter(a -> ObjectUtil.equal(a.getBOND_CODE(), userBondSubscription)).findAny();
            businessPrompt.ifPresent(response::add);
        }
        return response;
    }

    public RespBean<?> addUserBondSubscription(BaseRequest<AppBondSubscriptionReqBody> request) {
        if (StrUtil.isEmpty(request.getReqContent().getBondCode()) || StrUtil.isEmpty(request.getBase().getUid()) || StrUtil.isEmpty(request.getReqContent().getMobile())) {
            throw new AppException("用户ID或用户手机号或股票代码未传");
        }
        AppBondSubscription appBondSubscription = new AppBondSubscription();
        appBondSubscription.setBondCode(request.getReqContent().getBondCode());
        appBondSubscription.setUserId(request.getBase().getUid());
        appBondSubscription.setMobile(request.getReqContent().getMobile());
        AjaxResult ajaxResult = bondSubscriptionFeign.addUserBondSubscription(appBondSubscription);
        if (ObjectUtil.equals(ajaxResult.get("code"), 200)) {
            return RespBean.success();
        } else {
            return RespBean.error();
        }
    }


    public RespBean<?> cancelBondSubscription(BaseRequest<CancelBondSubscriptionReqBody> request) {
        if (ObjectUtil.isEmpty(request.getReqContent().getBondCode()) || StrUtil.isEmpty(request.getBase().getUid())) {
            throw new AppException("用户ID或股票代码未传");
        }
        AppBondSubscriptionPo appBondSubscriptionPo = new AppBondSubscriptionPo();
        appBondSubscriptionPo.setBondCode(request.getReqContent().getBondCode());
        appBondSubscriptionPo.setUserId(request.getBase().getUid());
        AjaxResult ajaxResult = bondSubscriptionFeign.cancelBondSubscription(appBondSubscriptionPo);
        if (!ObjectUtil.equals(ajaxResult.get("code"), 200)) {
            throw new AppException("删除失败");
        }
        return RespBean.success();
    }

    public JSONObject isSubscription(BaseRequest<IsSubscriptionReqBody> request) {
        if (StrUtil.isEmpty(request.getBase().getUid()) || StrUtil.isEmpty(request.getReqContent().getBondCode())) {
            throw new AppException("必传属性没有传");
        }
        JSONObject result = new JSONObject();
        List<String> userBondSubscriptions = bondSubscriptionFeign.getUserBondSubscription(request.getBase().getUid());
        for (String userBondSubscription : userBondSubscriptions) {
            if (StrUtil.equals(userBondSubscription, request.getReqContent().getBondCode())) {
                result.set("result", true);
                return result;
            }
        }
        result.set("result", false);
        return result;
    }

}
