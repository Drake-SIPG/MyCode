package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.HomePageIpoReqBody;
import com.sse.sseapp.form.request.IpoReqBody;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.mysoa.OptionalStockResponse;
import com.sse.sseapp.proxy.soa.SoaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ipo模块 service层
 *
 * @author zhengyaosheng
 * @date 2023-04-07
 */
@Service
@Slf4j
public class IpoService {

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    ISysConfigFeign sysConfigFeign;


    public RespBean<?> IPOList(BaseRequest<IpoReqBody> baseRequest) {
        IpoReqBody reqBody = baseRequest.getReqContent();
        Map<String, Object> param = new HashMap<>();
//        if (ObjectUtil.isEmpty(reqBody.getPageNo())) {
//            throw new AppException("PageNo参数必填");
//        }
//        if (ObjectUtil.isEmpty(reqBody.getPageSize())) {
//            throw new AppException("PageSize参数必填");
//        }
        param.put("pageNo", reqBody.getPageNo());
        param.put("pageSize", reqBody.getPageSize());
        param.put("stockCode", "");
        if (StrUtil.isEmpty(reqBody.getTradeBeginDate())) {
            param.put("tradeBeginDate", DateUtil.format(DateUtil.parse(reqBody.getDate(), DatePattern.NORM_DATE_FORMATTER), DatePattern.PURE_DATE_PATTERN));
        } else {
            param.put("tradeBeginDate", reqBody.getTradeBeginDate());
        }
        if (StrUtil.isEmpty(reqBody.getTradeEndDate())) {
            param.put("tradeEndDate", DateUtil.format(DateUtil.parse(reqBody.getDate(), DatePattern.NORM_DATE_FORMATTER), DatePattern.PURE_DATE_PATTERN));
        } else {
            param.put("tradeEndDate", reqBody.getTradeEndDate());
        }
        param.put("bizType", "1");
        param.put("order", "stockCode|asc,ext1|asc");
        //调用 获取服务器返回结果
        SoaResponse<MarketCalendarResBody> marketCalendarResult = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_IPO_LIST, param, baseRequest.getBase(), new TypeReference<SoaResponse<MarketCalendarResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, marketCalendarResult.getReturnCode())) {
            throw new AppException(marketCalendarResult.getReturnCode());
        }
        // 更改日期格式
        for (MarketCalendarResBody marketCalendarResBody : marketCalendarResult.getList()) {
            if (ObjectUtil.isNotEmpty(marketCalendarResBody.getTradeBeginDate())) {
                marketCalendarResBody.setTradeBeginDate(LocalDate.parse(marketCalendarResBody.getTradeBeginDate(), DateTimeFormatter.BASIC_ISO_DATE).toString());
            }
            if (ObjectUtil.isNotEmpty(marketCalendarResBody.getTradeEndDate())) {
                marketCalendarResBody.setTradeEndDate(LocalDate.parse(marketCalendarResBody.getTradeEndDate(), DateTimeFormatter.BASIC_ISO_DATE).toString());
            }
        }
        // 调用NewIpo接口
        param = new HashMap<>();
        param.put("pageNo", 1);
        param.put("listedStartDate", DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN));
        param.put("token", "APPMQUERY");
        param.put("pageSize", 0);
        param.put("tradeMarket", "SH");
        // 调用获取服务器返回结果
        SoaResponse<NewIpoResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_IPO_LIST, param, baseRequest.getBase(), new TypeReference<SoaResponse<NewIpoResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnCode());
        }
        // 更改日期格式
        List<NewIpoResBody> newResult = result.getList().stream().map(newIpoResBody -> {
            if (ObjectUtil.isNotEmpty(newIpoResBody.getOfflineIssuanceStartDate())) {
                newIpoResBody.setOfflineIssuanceStartDate(LocalDate.parse(newIpoResBody.getOfflineIssuanceStartDate(), DateTimeFormatter.BASIC_ISO_DATE).toString());
            }
            if (ObjectUtil.isNotEmpty(newIpoResBody.getOfflineIssuanceEndDate())) {
                newIpoResBody.setOfflineIssuanceEndDate(LocalDate.parse(newIpoResBody.getOfflineIssuanceEndDate(), DateTimeFormatter.BASIC_ISO_DATE).toString());
            }
            if (ObjectUtil.isNotEmpty(newIpoResBody.getOnlineIssuanceDate())) {
                newIpoResBody.setOnlineIssuanceDate(LocalDate.parse(newIpoResBody.getOnlineIssuanceDate(), DateTimeFormatter.BASIC_ISO_DATE).toString());
            }
            if (ObjectUtil.isNotEmpty(newIpoResBody.getPaymentStartDate())) {
                newIpoResBody.setPaymentStartDate(LocalDate.parse(newIpoResBody.getPaymentStartDate(), DateTimeFormatter.BASIC_ISO_DATE).toString());
            }
            if (ObjectUtil.isNotEmpty(newIpoResBody.getPaymentEndDate())) {
                newIpoResBody.setPaymentEndDate(LocalDate.parse(newIpoResBody.getPaymentEndDate(), DateTimeFormatter.BASIC_ISO_DATE).toString());
            }
            if (ObjectUtil.isNotEmpty(newIpoResBody.getAnnounceSuccRateRsDate())) {
                newIpoResBody.setAnnounceSuccRateRsDate(LocalDate.parse(newIpoResBody.getAnnounceSuccRateRsDate(), DateTimeFormatter.BASIC_ISO_DATE).toString());
            }
            return newIpoResBody;
        }).collect(Collectors.toList());
        // 用户是否已登录
        if (ObjectUtil.isNotEmpty(baseRequest.getBase().getUid())) {
            Map<String, Object> paramTwo = new HashMap<>();
            paramTwo.put("uid", baseRequest.getBase().getUid());
            paramTwo.put("token", "APPMQUERY");
            paramTwo.put("trademarket", "SH");
            paramTwo.put("accessToken", baseRequest.getBase().getAccessToken());
            OptionalStockResponse<OptionalStockResBody> optionalStockResponse = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_OPTIONAL_STOCK_LIST, paramTwo, baseRequest.getBase(), new TypeReference<OptionalStockResponse<OptionalStockResBody>>() {
            });
            if (!Objects.equals(1, optionalStockResponse.getStatus())) {
                throw new AppException(optionalStockResponse.getReason());
            }
            if (ObjectUtil.isNotEmpty(optionalStockResponse.getFollowCompanies()) && optionalStockResponse.getFollowCompanies().size() > 0) {
                List<String> stockCodeList = optionalStockResponse.getFollowCompanies().stream().map(OptionalStockResBody::getStockCode).collect(Collectors.toList());
                newResult = newResult.stream().map(newIpoResBody -> {
                    if (stockCodeList.contains(newIpoResBody.getStockCode())) {
                        newIpoResBody.setIsSelf(true);
                    }
                    return newIpoResBody;
                }).collect(Collectors.toList());
            }
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("ipoList", marketCalendarResult.getList());
        map.put("newIpoList", newResult);
        return RespBean.success(map);
    }

    public RespBean<?> newStockIPOList(BaseRequest<IpoReqBody> baseRequest) {

        IpoReqBody reqBody = baseRequest.getReqContent();
        Map<String, Object> param = BeanUtil.beanToMap(reqBody);
        if (ObjectUtil.isEmpty(param.get("bizType"))) {
            param.put("bizType", "1");
        }
        if (ObjectUtil.isEmpty(param.get("token"))) {
            param.put("token", "APPMQUERY");
        }
        if (ObjectUtil.isEmpty(param.get("order"))) {
            param.put("order", "stockCode|asc,ext1|asc");
        }

        //调用 获取服务器返回结果
        SoaResponse<IPOListResBody> marketCalendarResult = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_IPO_LIST, param, baseRequest.getBase(), new TypeReference<SoaResponse<IPOListResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, marketCalendarResult.getReturnCode())) {
            throw new AppException(marketCalendarResult.getReturnCode());
        }
        return RespBean.success(addType(marketCalendarResult));
    }

    public RespBean<?> getHomePageIpo(BaseRequest<HomePageIpoReqBody> body) {

        Map<String, Object> param = BeanUtil.beanToMap(body.getReqContent());
        if (ObjectUtil.isEmpty(param.get("pageSize"))) {
            param.put("pageSize", "0");
        }
        if (ObjectUtil.isEmpty(param.get("token"))) {
            param.put("token", "APPMQUERY");
        }
        if (ObjectUtil.isEmpty(param.get("tradeMarket"))) {
            param.put("tradeMarket", "SH");
        }
        if (ObjectUtil.isEmpty(param.get("order"))) {
            param.put("order", "onlineIssuanceDate|asc,id|asc");
        }

        //调用 获取服务器返回结果
        SoaResponse<GetIpoListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_IPO_LIST, param, body.getBase(), new TypeReference<SoaResponse<GetIpoListResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnCode());
        }
        for (GetIpoListResBody newSharesDataResBody : result.getList()) {
            String code = newSharesDataResBody.getStockCode();
            if (code.startsWith("6")) {
                newSharesDataResBody.setType("EQU");
                newSharesDataResBody.setSubType("ASH");
            } else if (code.startsWith("9")) {
                newSharesDataResBody.setType("EQU");
                newSharesDataResBody.setSubType("BSH");
            } else if (code.startsWith("5")) {
                newSharesDataResBody.setType("FUN");
                newSharesDataResBody.setType("LOF");
            } else {
                newSharesDataResBody.setType("BON");
                newSharesDataResBody.setSubType("CBD");
            }
        }
        return RespBean.success(result);
    }

    public SoaResponse<IPOListResBody> addType(SoaResponse<IPOListResBody> obj) {
        // 6开头 -> EQU+ASH
        // 9开头 -> EQU+BSH
        String roadShowUrl = sysConfigFeign.getConfigKey(AppConstants.SYS_CONFIG_KEY_RoadShowUrl);
        List<IPOListResBody> list = obj.getList();
        String code = "";
        String bizType = "";
        for (IPOListResBody ipoListResBody : list) {
            code = ipoListResBody.getStockCode();
            bizType = ipoListResBody.getBizType();
            if ("2".equals(bizType) || "9".equals(bizType)) {
                ipoListResBody.setRsurl(roadShowUrl + "?rsId=" + ipoListResBody.getBizSeq());
            }
            if ("8".equals(bizType)) {
                ipoListResBody.setSummary(GetSummary(ipoListResBody.getExt1(), ipoListResBody.getSummary()));
            }
            if (code.startsWith("6")) {
                ipoListResBody.setType("EQU");
                ipoListResBody.setSubtype("ASH");
            } else if (code.startsWith("9")) {
                ipoListResBody.setType("EQU");
                ipoListResBody.setSubtype("BSH");
            } else if (code.startsWith("5")) {
                ipoListResBody.setType("FUN");
                ipoListResBody.setSubtype("LOF");
            } else {
                ipoListResBody.setType("BON");
                ipoListResBody.setSubtype("CBD");
            }
            //如果是不展示的股票代码不放到返回结果内
            if (AppConstants.STOCK_CODE_DELETE_LIST.contains(code)) {
                list.remove(ipoListResBody);
            }
        }
        obj.setList(list);
        return obj;
    }

    private static String GetSummary(Object ext1, Object o) {
        String result = "";
        if (o == null || ext1 == null) {
            return "";
        }

        result += "<table cellspacing=0 ><tr><th>报告类型</th><th>" + ext1.toString() + "</th></tr>";
        String[] summary = o.toString().split(",");
        for (int i = 0; i < summary.length; i++) {
            result += "<tr><td>" + summary[i].substring(0, summary[i].indexOf(":")) + "</td><td>" + summary[i].substring(summary[i].indexOf(":") + 1) + "</td></tr>";
        }
        result += "</table>";
        return result;
    }
}
