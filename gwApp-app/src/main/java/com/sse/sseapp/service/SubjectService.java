package com.sse.sseapp.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.form.request.MarginTradingNoticeReqBody;
import com.sse.sseapp.form.request.MarginTradingReqBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import com.sse.sseapp.proxy.soa.dto.CirculationMarketValueDto;
import com.sse.sseapp.proxy.soa.dto.MarginTradingNoticeDto;
import com.sse.sseapp.proxy.soa.dto.TurnoverSurveyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class SubjectService {

    @Autowired
    private ProxyProvider proxyProvider;

    @Autowired
    private CommonService commonService;

    public RespBean<?> marginTrading(BaseRequest<MarginTradingReqBody> reqBody) {
        try {
            Map<String, Object> prams = new HashMap<>();
            prams.put("pageNo", 1);
            prams.put("pageSize", 4);
            //通知公告
            Object o1 = marginTradingNotice(prams, reqBody.getBase());
            prams.clear();
            prams.put("pageNo", 1);
            prams.put("pageSize", 4);
            //流通市值占比
            Object o2 = circulationMarketValue(prams, reqBody.getBase());
            prams.clear();
            prams.put("pageNo", 1);
            prams.put("pageSize", 4);
            //融资融券余额走势图
            Object o3 = returnmarginBalanceChart(reqBody.getBase());
            prams.clear();
            prams = BeanUtil.beanToMap(reqBody.getReqContent());
            //成交概况
            Object o4 = turnoverSurvey(prams, reqBody.getBase());
            Map<String, Object> o = new HashMap<>();
            Map<String, Object> m = new HashMap<>();
            m.put("gg", o1);
            m.put("ltsz", o2);
            m.put("zst", o3);
            m.put("cjgk", o4);
            o.put("data", m);
            return RespBean.success(o);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    public RespBean<?> marginTradingCMS(BaseRequest<MarginTradingReqBody> reqBody) {
        try {
            Map<String, Object> prams = new HashMap<>();
            prams.put("pageNo", 1);
            prams.put("pageSize", 4);
            //通知公告
            Object o1 = marginTradingNoticeCMS(prams, reqBody.getBase());
            prams.clear();
            prams.put("pageNo", 1);
            prams.put("pageSize", 4);
            //流通市值占比
            Object o2 = circulationMarketValue(prams, reqBody.getBase());
            prams.clear();
            prams.put("pageNo", 1);
            prams.put("pageSize", 4);
            //融资融券余额走势图
            Object o3 = returnmarginBalanceChart(reqBody.getBase());
            prams.clear();
            prams = BeanUtil.beanToMap(reqBody.getReqContent());
            //成交概况
            Object o4 = turnoverSurvey(prams, reqBody.getBase());
            Map<String, Object> o = new HashMap<>();
            Map<String, Object> m = new HashMap<>();
            m.put("gg", o1);
            m.put("ltsz", o2);
            m.put("zst", o3);
            m.put("cjgk", o4);
            o.put("data", m);
            return RespBean.success(o);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    private SoaResponse<MarginTradingNoticeDto> marginTradingNotice(Map<String, Object> param, ReqBaseVO base) {
        Map<String, String> configMap = commonService.getActive("marginTrading");
        param.put("channelId", configMap.get("default"));
        param.put("siteId", "28");
        param.put("order", "createTime|desc,docId|desc");
        // 调用 获取服务器返回结果
        SoaResponse<MarginTradingNoticeDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST, param, base, new TypeReference<SoaResponse<MarginTradingNoticeDto>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    private SoaResponse<MarginTradingNoticeDto> marginTradingNoticeCMS(Map<String, Object> param, ReqBaseVO base) {
        Map<String, String> configMap = commonService.getActive("marginTrading");
        param.put("channelId", configMap.get("default"));
        param.put("siteId", "28");
        param.put("order", "createTime|desc,docId|desc");
        // 调用 获取服务器返回结果
        SoaResponse<MarginTradingNoticeDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST_CMS, param, base, new TypeReference<SoaResponse<MarginTradingNoticeDto>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    private SoaResponse<CirculationMarketValueDto> circulationMarketValue(Map<String, Object> param, ReqBaseVO base) {
        param.put("tradeDate", "max");
        // 调用 获取服务器返回结果
        SoaResponse<CirculationMarketValueDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_CIRCULATION_MARKET_VALUE, param, base, new TypeReference<SoaResponse<CirculationMarketValueDto>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    public Object returnmarginBalanceChart(ReqBaseVO base) {
        try {
            Map<String, Object> res = new HashMap<>();
            Map<String, Object> newmap = new HashMap<>();
            List<Object> l = new ArrayList<>();
            List<Object> d = new ArrayList<>();
            SoaResponse<Map<String, Object>> map = marginBalanceChart(new HashMap<>(), base);
            List<Map<String, Object>> list = map.getList();
            DecimalFormat df = new DecimalFormat("#.00");
            for (int i = list.size() - 1; i >= 0; i--) {
                Map<String, Object> item = list.get(i);
                double marginBalance = (double) Long.parseLong(item.get("marginBalance").toString());
                l.add(df.format(marginBalance / 10000000));
                if (i == 0 || i == 7 || i == 15 || i == 22 || i == 29) {
                    String tradeDate = item.get("tradeDate").toString();
                    d.add(tradeDate.substring(4, 6) + "/" + tradeDate.substring(6, 8));
                }
            }
            newmap.put("datas", l);
            newmap.put("labels", d);
            res.put("data", newmap);
            return res;
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    private SoaResponse<Map<String, Object>> marginBalanceChart(Map<String, Object> param, ReqBaseVO base) {
        param.put("tradeDate", "all");
        param.put("pageSize", "30");
        param.put("fields", "tradeDate,marginBalance");
        // 调用 获取服务器返回结果
        SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_MARGIN_BALANCE_CHART, param, base, new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result;
    }

    private SoaResponse<TurnoverSurveyDto> turnoverSurvey(Map<String, Object> param, ReqBaseVO base) {
        // 调用 获取服务器返回结果
        SoaResponse<TurnoverSurveyDto> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_TURNOVER_SURVEY, param, base, new TypeReference<SoaResponse<TurnoverSurveyDto>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        List<TurnoverSurveyDto> list = result.getList();
        for (TurnoverSurveyDto turnoverSurvey : list) {
            turnoverSurvey.setFinancingAmount(NumberUtil.decimalFormat("#,###", NumberUtil.parseNumber(turnoverSurvey.getFinancingAmount())));
            turnoverSurvey.setFinancingBalance(NumberUtil.decimalFormat("#,###", NumberUtil.parseNumber(turnoverSurvey.getFinancingBalance())));
            turnoverSurvey.setLendingBalance(NumberUtil.decimalFormat("#,###", NumberUtil.parseNumber(turnoverSurvey.getLendingBalance())));
            turnoverSurvey.setLendingBalanceValue(NumberUtil.decimalFormat("#,###", NumberUtil.parseNumber(turnoverSurvey.getLendingBalanceValue())));
            turnoverSurvey.setLendingSellVolumn(NumberUtil.decimalFormat("#,###", NumberUtil.parseNumber(turnoverSurvey.getLendingSellVolumn())));
            turnoverSurvey.setMarginBalance(NumberUtil.decimalFormat("#,###", NumberUtil.parseNumber(turnoverSurvey.getMarginBalance())));
        }
        return result;
    }

    public SoaResponse<MarginTradingNoticeDto> marginTradingNotice(BaseRequest<MarginTradingNoticeReqBody> reqBody) {
        Map<String, Object> param = BeanUtil.beanToMap(reqBody.getReqContent());
        SoaResponse<MarginTradingNoticeDto> response = proxyProvider.proxy(ApiCodeConstants.SYS_MARGIN_TRADING_NOTICE, param, reqBody.getBase(), new TypeReference<SoaResponse<MarginTradingNoticeDto>>() {
        });
        if (!StrUtil.equals(response.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException("请求返回结果失败，请稍后再试");
        }
        return response;
    }

    public SoaResponse<MarginTradingNoticeDto> marginTradingNoticeCMS(BaseRequest<MarginTradingNoticeReqBody> reqBody) {
        Map<String, Object> param = BeanUtil.beanToMap(reqBody.getReqContent());
        SoaResponse<MarginTradingNoticeDto> response = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST_CMS, param, reqBody.getBase(), new TypeReference<SoaResponse<MarginTradingNoticeDto>>() {
        });
        if (!StrUtil.equals(response.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException("请求返回结果失败，请稍后再试");
        }
        return response;
    }
}
