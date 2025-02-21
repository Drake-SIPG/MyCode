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
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.utils.Util;
import com.sse.sseapp.domain.push.AppBondPull;
import com.sse.sseapp.domain.system.SysProxyConfig;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.feign.system.ISysProxyFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyConfig;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.mysoa.OptionalStockResponse;
import com.sse.sseapp.proxy.mysoa.dto.OptionalstockListDto;
import com.sse.sseapp.proxy.query.QueryResponse;
import com.sse.sseapp.proxy.soa.SoaResponse;
import com.sse.sseapp.util.VersionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalendarService {


    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    CommonService commonService;

    @Autowired
    PersonService personService;

    @Autowired
    ISysProxyFeign sysProxyFeign;

    @Autowired
    ISysConfigFeign sysConfigFeign;


    public RespBean<?> calendarList(BaseRequest<CalendarListReqBody> baseRequest) {
        CalendarListReqBody calendarListReqBody = baseRequest.getReqContent();
        // 股票代码
        String code = calendarListReqBody.getCode();
        if (ObjectUtil.isNotEmpty(calendarListReqBody.getIsSelf()) && calendarListReqBody.getIsSelf()) {
            commonService.cominfoCheck(baseRequest.getBase());
            code = getStockCode(baseRequest);
            if (StrUtil.isEmpty(code)) {
                return RespBean.success(new HashMap<String, Object>());
            }
        }
        // 是否是交易日
        Boolean isTradeDay = calendarListReqBody.getIsTradeDay();
        // 日期类型
        String date = calendarListReqBody.getDate();
        if (StrUtil.isEmpty(date)) {
            date = Util.getDate("yyyyMMdd");
        }
        String datatype = calendarListReqBody.getDatatype();
        Boolean dataSelf = calendarListReqBody.getIsSelf();
        Map<String, Object> o = new HashMap<>();
        if (ObjectUtil.isNotEmpty(datatype) && "ALL".equalsIgnoreCase(datatype)) {
            if (baseRequest.getBase().getAv() != null) {
                datatype = "2_3_4_6_8_9";
                //去除E访谈 datatype = "1_2_3_4_5_6_8_9";
                //从日历页面进入 去除停复牌
            } else {
                datatype = "1_2_3_4_6";
                //去除E访谈 datatype = "1_2_3_4_5_6";
            }
        }
        String[] dataTypes = datatype.split("_");
        Map<String, Object> params = new HashMap<>();
        params.put("tradeBeginDate", date);
        params.put("tradeEndDate", date);
        params.put("stockCode", code);
        if (ObjectUtil.isNotEmpty(code) && !dataSelf) {
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.add(java.util.Calendar.MONTH, -6);
            // 开始时间要六个月前的
            params.put("tradeBeginDate",
                    Util.getDate(c.getTime(), "yyyyMMdd"));
            c = java.util.Calendar.getInstance();
            c.add(java.util.Calendar.YEAR, 10);
            // 结束时间要 没有底线 没有默认值 设置了 加10 年
            params.put("tradeEndDate", Util.getDate(c.getTime(), "yyyyMMdd"));
            params.put("order", "tradeBeginDate|desc");
        }
        params.put("pageNo", 1);
        params.put("pageSize", 5);
        List<Map<String, Object>> list = new ArrayList<>();
        for (String dataType : dataTypes) {
            if ("1".equals(dataType)) {
                if (ObjectUtil.isNotEmpty(isTradeDay) && !isTradeDay) {
                    continue;
                }
                Map<String, Object> map = getParamsCalendar(params, "suspensionList");
                // 获取暂停列表
                // 调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> sl = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                Integer slTotal = sl.getTotal();
                // 停牌
                if (slTotal != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", slTotal);
                    item.put("title", "停复牌");
                    item.put("list", addType(sl.getList()));
                    item.put("type", "1");
                    list.add(item);
                }
            } else if ("2".equals(dataType)) {
                // 分红派息除权
                Map<String, Object> map = getParamsCalendar(params, "bonusDividendExList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> blFH = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                Integer blFHTotal = blFH.getTotal();
                // 送股
                Map<String, Object> mapTwo = getParamsCalendar(params, "bonusList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> blSG = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, mapTwo);
                int blSGTotal = blSG.getTotal();
                // 分红派息除权
                int blTotal = blFHTotal + blSGTotal;
                if (blTotal != 0) {
                    List<MarketCalendarResBody> bllist = new ArrayList<>();
                    if (blFHTotal != 0) {
                        List<MarketCalendarResBody> blFHList = blFH.getList();
                        bllist.addAll(blFHList);
                    }
                    if (blSGTotal != 0) {
                        List<MarketCalendarResBody> blSGList = blSG.getList();
                        bllist.addAll(blSGList);
                    }
                    // 两边都有 两边都有会有时间 不排序的情况
                    if (blFHTotal != 0 && blSGTotal != 0) {
                        // 这里要排序 真恶心
                        boolean exchange;
                        for (int k = 0; k < bllist.size(); k++) {
                            MarketCalendarResBody temp;
                            exchange = false;
                            for (int j = bllist.size() - 2; j >= k; j--) {
                                String sbd1 = Util.getObjStrV((bllist.get(j + 1).getTradeBeginDate()));
                                String sbd2 = Util.getObjStrV((bllist.get(j).getTradeBeginDate()));
                                if (sbd1.compareTo(sbd2) >= 0) {
                                    temp = bllist.get(j + 1);
                                    bllist.set(j + 1, bllist.get(j));
                                    bllist.set(j, temp);
                                    exchange = true;
                                }
                            }
                            if (!exchange) {
                                break;
                            }
                        }
                    }
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", blTotal);
                    item.put("title", "分红送转/除权除息");
                    item.put("list", addType(bllist));
                    item.put("type", "2");
                    list.add(item);
                }
            } else if ("3".equals(dataType)) {
                if (ObjectUtil.isNotEmpty(isTradeDay) && !isTradeDay) {
                    continue;
                }
                Map<String, Object> map = getParamsCalendar(params, "IPOList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> IPOl = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                Map<String, Object> mapTwo = getParamsCalendar(params, "IPOListTwo");
                mapTwo.put("pageSize", 0);
                SoaResponse<MarketCalendarResBody> IPOToal = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, mapTwo);
                int IPOl_total = IPOToal.getList().size();
                // ipo
                if (IPOl_total != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", IPOl_total);
                    item.put("title", "IPO信息");
                    item.put("list", addType(IPOl.getList()));
                    item.put("type", "3");
                    list.add(item);
                }
            } else if ("4".equals(dataType)) {
                String version = baseRequest.getBase().getAppVersion();
                if(!VersionUtil.geTargetVersion(version, "5.3.0")) {
                    Map<String, Object> map = getParamsCalendar(params, "shareholdersMeetingList");
                    //调用 获取服务器返回结果
                    SoaResponse<MarketCalendarResBody> sml = marketCalendarRequest(baseRequest.getBase(),
                            ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                    int smlTotal = sml.getTotal();
                    // 股东大会信息
                    if (smlTotal != 0) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("url", "/calendar/shareholdersMeetingList");
                        item.put("total", smlTotal);
                        item.put("title", "股东大会");
                        item.put("list", addType(sml.getList()));
                        item.put("type", "4");
                        list.add(item);
                    }
                } else {
                    Map<String, Object> data = new HashMap<>();
                    data.put("conveneDate" ,baseRequest.getReqContent().getDate());
                    data.put("secCode", baseRequest.getReqContent().getCode());
                    data.put("sqlId", "COMMON_SSE_SCFW_TZZFW_GDDHWLTPZL_L");
                    //调用 获取服务器返回结果
                    QueryResponse<ShareholdersMeetingListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, baseRequest.getBase(), new TypeReference<QueryResponse<ShareholdersMeetingListResBody>>() {
                    });
                    // 股东大会信息
                    if (!ObjectUtil.equal(result.getSuccess(), "false")) {
                        if (result.getResult().size() > 0) {
                            Map<String, Object> item = new HashMap<>();
                            item.put("total", result.getResult().size());
                            item.put("url", "/calendar/shareholdersMeetingList");
                            item.put("title", "股东大会");
                            item.put("list", result);
                            item.put("type", "4");
                            list.add(item);
                        }
                    }
                }
            } else if ("5".equals(dataType)) {
                Map<String, Object> map = getParamsCalendar(params, "eInterviewList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> eil = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                int eilTotal = eil.getTotal();
                // E访谈信息
                if (eilTotal != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", eilTotal);
                    item.put("title", "E访谈");
                    item.put("list", addType(eil.getList()));
                    item.put("type", "5");
                    list.add(item);
                }
            } else if ("6".equals(dataType)) {
                Map<String, Object> map = getParamsCalendar(params, "roadshowList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> rsl = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                int rslTotal = rsl.getTotal();
                // 路演信息
                if (rslTotal != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", rslTotal);
                    item.put("title", "路演");
                    item.put("list", addType(rsl.getList()));
                    item.put("type", "6");
                    list.add(item);
                }
            } else if ("8".equals(dataType)) {
                Map<String, Object> map = getParamsCalendar(params, "reportAppointment");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> rsl = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                int rslTotal = rsl.getTotal();
                // 定期报告预约信息
                if (rslTotal != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", rslTotal);
                    item.put("title", "定期报告预约");
                    item.put("list", addType(rsl.getList()));
                    item.put("type", "8");
                    list.add(item);
                }
            } else if ("9".equals(dataType)) {
                Map<String, Object> map = getParamsCalendar(params, "activityInfo");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> rsl = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                int rslTotal = rsl.getTotal();
                // 活动信息
                if (rslTotal != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", rslTotal);
                    item.put("title", "活动");
                    item.put("list", addType(rsl.getList()));
                    item.put("type", "9");
                    list.add(item);
                }
            }
        }
        o.put("list", list);
        return RespBean.success(o);
    }

    public RespBean<?> companyCalendarList(BaseRequest<CalendarListReqBody> baseRequest) {
        CalendarListReqBody calendarListReqBody = baseRequest.getReqContent();
        // 股票代码
        String code = calendarListReqBody.getCode();
        if (ObjectUtil.isNotEmpty(calendarListReqBody.getIsSelf()) && calendarListReqBody.getIsSelf()) {
            commonService.cominfoCheck(baseRequest.getBase());
            code = getStockCode(baseRequest);
            if (StrUtil.isEmpty(code)) {
                return RespBean.success(new HashMap<String, Object>());
            }
        }
        // 是否是交易日
        Boolean isTradeDay = calendarListReqBody.getIsTradeDay();
        // 日期类型
        String date = calendarListReqBody.getDate();
        if (StrUtil.isEmpty(date)) {
            date = Util.getDate("yyyyMMdd");
        }
        String datatype = calendarListReqBody.getDatatype();
        Boolean dataSelf = calendarListReqBody.getIsSelf();
        Map<String, Object> o = new HashMap<>();
        if (ObjectUtil.isNotEmpty(datatype) && "ALL".equalsIgnoreCase(datatype)) {
            if (baseRequest.getBase().getAv() != null) {
                datatype = "2_3_4_6_8_9";
                //去除E访谈 datatype = "1_2_3_4_5_6_8_9";
                //从日历页面进入 去除停复牌
            } else {
                datatype = "1_2_3_4_6";
                //去除E访谈 datatype = "1_2_3_4_5_6";
            }
        }
        String[] dataTypes = datatype.split("_");
        Map<String, Object> params = new HashMap<>();
        params.put("tradeBeginDate", date);
        params.put("tradeEndDate", date);
        params.put("stockCode", code);
        if (ObjectUtil.isNotEmpty(code) && !dataSelf) {
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.add(java.util.Calendar.MONTH, -6);
            // 开始时间要六个月前的
            params.put("tradeBeginDate",
                    Util.getDate(c.getTime(), "yyyyMMdd"));
            c = java.util.Calendar.getInstance();
            c.add(java.util.Calendar.YEAR, 10);
            // 结束时间要 没有底线 没有默认值 设置了 加10 年
            params.put("tradeEndDate", Util.getDate(c.getTime(), "yyyyMMdd"));
            params.put("order", "tradeBeginDate|desc");
        }
        params.put("pageNo", 1);
        params.put("pageSize", 5);
        List<Map<String, Object>> list = new ArrayList<>();
        for (String dataType : dataTypes) {
            if ("1".equals(dataType)) {
                if (ObjectUtil.isNotEmpty(isTradeDay) && !isTradeDay) {
                    continue;
                }
                Map<String, Object> map = getParamsCalendar(params, "suspensionList");
                // 获取暂停列表
                // 调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> sl = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                Integer slTotal = sl.getTotal();
                // 停牌
                if (slTotal != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", slTotal);
                    item.put("title", "停复牌");
                    item.put("list", addType(sl.getList()));
                    item.put("type", "1");
                    list.add(item);
                }
            } else if ("2".equals(dataType)) {
                // 分红派息除权
                Map<String, Object> map = getParamsCalendar(params, "bonusDividendExList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> blFH = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                Integer blFHTotal = blFH.getTotal();
                // 送股
                Map<String, Object> mapTwo = getParamsCalendar(params, "bonusList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> blSG = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, mapTwo);
                int blSGTotal = blSG.getTotal();
                // 分红派息除权
                int blTotal = blFHTotal + blSGTotal;
                if (blTotal != 0) {
                    List<MarketCalendarResBody> bllist = new ArrayList<>();
                    if (blFHTotal != 0) {
                        List<MarketCalendarResBody> blFHList = blFH.getList();
                        bllist.addAll(blFHList);
                    }
                    if (blSGTotal != 0) {
                        List<MarketCalendarResBody> blSGList = blSG.getList();
                        bllist.addAll(blSGList);
                    }
                    // 两边都有 两边都有会有时间 不排序的情况
                    if (blFHTotal != 0 && blSGTotal != 0) {
                        // 这里要排序 真恶心
                        boolean exchange;
                        for (int k = 0; k < bllist.size(); k++) {
                            MarketCalendarResBody temp;
                            exchange = false;
                            for (int j = bllist.size() - 2; j >= k; j--) {
                                String sbd1 = Util.getObjStrV((bllist.get(j + 1).getTradeBeginDate()));
                                String sbd2 = Util.getObjStrV((bllist.get(j).getTradeBeginDate()));
                                if (sbd1.compareTo(sbd2) >= 0) {
                                    temp = bllist.get(j + 1);
                                    bllist.set(j + 1, bllist.get(j));
                                    bllist.set(j, temp);
                                    exchange = true;
                                }
                            }
                            if (!exchange) {
                                break;
                            }
                        }
                    }
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", blTotal);
                    item.put("title", "分红送转/除权除息");
                    item.put("list", addType(bllist));
                    item.put("type", "2");
                    list.add(item);
                }
            } else if ("3".equals(dataType)) {
                if (ObjectUtil.isNotEmpty(isTradeDay) && !isTradeDay) {
                    continue;
                }
                Map<String, Object> map = getParamsCalendar(params, "IPOList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> IPOl = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                Map<String, Object> mapTwo = getParamsCalendar(params, "IPOListTwo");
                mapTwo.put("pageSize", 0);
                SoaResponse<MarketCalendarResBody> IPOToal = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, mapTwo);
                int IPOl_total = IPOToal.getList().size();
                // ipo
                if (IPOl_total != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", IPOl_total);
                    item.put("title", "IPO信息");
                    item.put("list", addType(IPOl.getList()));
                    item.put("type", "3");
                    list.add(item);
                }
            } else if ("4".equals(dataType)) {
                Map<String, Object> map = getParamsCalendar(params, "shareholdersMeetingList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> sml = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                int smlTotal = sml.getTotal();
                // 股东大会信息
                if (smlTotal != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("url", "/calendar/shareholdersMeetingList");
                    item.put("total", smlTotal);
                    item.put("title", "股东大会");
                    item.put("list", addType(sml.getList()));
                    item.put("type", "4");
                    list.add(item);
                }
            } else if ("5".equals(dataType)) {
                Map<String, Object> map = getParamsCalendar(params, "eInterviewList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> eil = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                int eilTotal = eil.getTotal();
                // E访谈信息
                if (eilTotal != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", eilTotal);
                    item.put("title", "E访谈");
                    item.put("list", addType(eil.getList()));
                    item.put("type", "5");
                    list.add(item);
                }
            } else if ("6".equals(dataType)) {
                Map<String, Object> map = getParamsCalendar(params, "roadshowList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> rsl = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                int rslTotal = rsl.getTotal();
                // 路演信息
                if (rslTotal != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", rslTotal);
                    item.put("title", "路演");
                    item.put("list", addType(rsl.getList()));
                    item.put("type", "6");
                    list.add(item);
                }
            } else if ("8".equals(dataType)) {
                Map<String, Object> map = getParamsCalendar(params, "reportAppointment");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> rsl = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                int rslTotal = rsl.getTotal();
                // 定期报告预约信息
                if (rslTotal != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", rslTotal);
                    item.put("title", "定期报告预约");
                    item.put("list", addType(rsl.getList()));
                    item.put("type", "8");
                    list.add(item);
                }
            } else if ("9".equals(dataType)) {
                Map<String, Object> map = getParamsCalendar(params, "activityInfo");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> rsl = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                int rslTotal = rsl.getTotal();
                // 活动信息
                if (rslTotal != 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("total", rslTotal);
                    item.put("title", "活动");
                    item.put("list", addType(rsl.getList()));
                    item.put("type", "9");
                    list.add(item);
                }
            }
        }
        o.put("list", list);
        return RespBean.success(o);
    }

    private SoaResponse<MarketCalendarResBody> marketCalendarRequest(ReqBaseVO base, String sysProxyCode, Map<String, Object> params) {
        //调用 获取服务器返回结果
        SoaResponse<MarketCalendarResBody> result = proxyProvider.proxy(sysProxyCode, params, base, new TypeReference<SoaResponse<MarketCalendarResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException("获取数据失败,请稍后再试");
        }
        result.setList(addType(result.getList()));
        return result;
    }

    public List<CalendarDayListResBody> getCalendarDayListByDateRange(BaseRequest<CalendarDayListReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (data.containsKey("tradeBeginDate")) {
            data.put("beginDate", data.remove("tradeBeginDate"));
        }
        if (data.containsKey("tradeEndDate")) {
            data.put("endDate", data.remove("tradeEndDate"));
        }
        SoaResponse<CalendarDayListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_CALENDAR_DAY_LIST_BY_DATE_RANGE, data, baseRequest.getBase(), new TypeReference<SoaResponse<CalendarDayListResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result.getList();
    }

    /**
     * 行情-详情页面-日历(股东大会)
     */
    public Object shareholdersMeetingList(BaseRequest<ShareholdersMeetingListReqBody> calendarListDtoReqBody) {
        BaseRequest<CalendarListReqBody> baseRequest = new BaseRequest<>();

        CalendarListReqBody reqBody = new CalendarListReqBody();
        reqBody.setCode(calendarListDtoReqBody.getReqContent().getStockCode());
        reqBody.setDatatype("1_2_3_4_8_9");
        reqBody.setIsTradeDay(true);
        reqBody.setDate(calendarListDtoReqBody.getReqContent().getTradeEndDate());
        reqBody.setIsSelf(false);

        baseRequest.setBase(calendarListDtoReqBody.getBase());
        baseRequest.setReqContent(reqBody);
        RespBean<?> respBean = companyCalendarList(baseRequest);
//        Map<String, Object> data = BeanUtil.beanToMap(calendarListDtoReqBody.getReqContent());
//        data.put("tradeBeginDate", DateUtil.offset(DateUtil.parseDate(DateUtil.today()), DateField.MONTH, -6).toString(DatePattern.PURE_DATE_PATTERN));
//        data.put("tradeEndDate", DateUtil.parse(DateUtil.today()).toString(DatePattern.PURE_DATE_PATTERN));
//        ShareholdersMeetingListDto result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SHAREHOLDERS_MEETING_LIST, data, null, new TypeReference<ShareholdersMeetingListDto>() {
//        });
//        //转换日期格式
//        for (ShareholdersMeetingListDto.ShareholdersMeetingListRes list : result.getList()) {
//            list.setTradeBeginDate(DateUtil.parse(list.getTradeBeginDate()).toString(DatePattern.NORM_DATE_PATTERN));
//            list.setTradeEndDate(DateUtil.parse(list.getTradeEndDate()).toString(DatePattern.NORM_DATE_PATTERN));
//        }
//        //如果也没有错，也没有股东大会集合，说明就没有近半年的股东大会，返回null
        return respBean.getData();
    }

    /**
     * 行情-详情页面-大宗
     */
    public SoaResponse<GetBlockTradeDataResBody> getBlockTradeData(BaseRequest<GetBlockTradeDataReqBody> getBlockTradeDataReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(getBlockTradeDataReqBody.getReqContent());
        data.put("token", "APPMQUERY");
        data.put("tradeEndDate", DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN));
        data.put("pageNo", data.remove("page"));
        SoaResponse<GetBlockTradeDataResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_BLOCK_TRADE_DATA, data, null, new TypeReference<SoaResponse<GetBlockTradeDataResBody>>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException(result.getReturnMsg());
        }
        //设置日期格式
        for (GetBlockTradeDataResBody list : result.getList()) {
            list.setTradeDate(DateUtil.parse(list.getTradeDate()).toString(DatePattern.NORM_DATE_PATTERN));
        }
        return result;
    }

    public List<MarketCalendarResBody> getMarketCalendarList(BaseRequest<MarketCalendarListReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (ObjectUtil.isEmpty(data.get("type"))) {
            SoaResponse<MarketCalendarResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, data, baseRequest.getBase(), new TypeReference<SoaResponse<MarketCalendarResBody>>() {
            });
            if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                throw new AppException(result.getReturnMsg());
            }
            return result.getList();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("tradeBeginDate", data.get("tradeBeginDate"));
        params.put("tradeEndDate", data.get("tradeEndDate"));
        params.put("stockCode", data.get("stockCode"));
        params.put("pageNo", data.get("pageNo"));
        params.put("pageSize", data.get("pageSize"));
        //如果是自选 添加自选股
        if (baseRequest.getReqContent().getSelf()) {
            StringBuilder code = new StringBuilder();
            BaseRequest<OptionalStockReqBody> option = new BaseRequest<>();
            option.setBase(baseRequest.getBase());
            option.setReqContent(new OptionalStockReqBody());
            OptionalStockResponse<OptionalStockResBody> response = (OptionalStockResponse<OptionalStockResBody>) personService.optionalStockList(option).getData();
            for (OptionalStockResBody followCompany : response.getFollowCompanies()) {
                if (code.length() < 7) {
                    code.append(followCompany.getStockCode()).append(",");
                }
            }
            if (StrUtil.isNotEmpty(code) && ObjectUtil.equals(code.lastIndexOf(","), code.length() - 1)) {
                code.replace(code.length() - 1, code.length(), "");
            }
            params.put("stockCode", code);
        }
        Map<String, Object> map;
        switch (data.get("type").toString()) {
            case "1":
                map = getParamsCalendar(params, "suspensionList");
                return marketCalendarRequest(baseRequest.getBase(), ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map).getList();
            case "2":
                // 分红派息除权
                map = getParamsCalendar(params, "bonusDividendExList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> blFH = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map);
                Integer blFHTotal = blFH.getTotal();
                // 送股
                Map<String, Object> mapTwo = getParamsCalendar(params, "bonusList");
                //调用 获取服务器返回结果
                SoaResponse<MarketCalendarResBody> blSG = marketCalendarRequest(baseRequest.getBase(),
                        ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, mapTwo);
                int blSGTotal = blSG.getTotal();
                // 分红派息除权
                int blTotal = blFHTotal + blSGTotal;
                if (blTotal != 0) {
                    List<MarketCalendarResBody> bllist = new ArrayList<>();
                    if (blFHTotal != 0) {
                        List<MarketCalendarResBody> blFHList = blFH.getList();
                        bllist.addAll(blFHList);
                    }
                    if (blSGTotal != 0) {
                        List<MarketCalendarResBody> blSGList = blSG.getList();
                        bllist.addAll(blSGList);
                    }
                    // 两边都有 两边都有会有时间 不排序的情况
                    if (blFHTotal != 0 && blSGTotal != 0) {
                        // 这里要排序 真恶心
                        boolean exchange;
                        for (int k = 0; k < bllist.size(); k++) {
                            MarketCalendarResBody temp;
                            exchange = false;
                            for (int j = bllist.size() - 2; j >= k; j--) {
                                String sbd1 = Util.getObjStrV((bllist.get(j + 1).getTradeBeginDate()));
                                String sbd2 = Util.getObjStrV((bllist.get(j).getTradeBeginDate()));
                                if (sbd1.compareTo(sbd2) >= 0) {
                                    temp = bllist.get(j + 1);
                                    bllist.set(j + 1, bllist.get(j));
                                    bllist.set(j, temp);
                                    exchange = true;
                                }
                            }
                            if (!exchange) {
                                break;
                            }
                        }
                    }
                    return bllist;
                }
                break;
            case "3":
                map = getParamsCalendar(params, "IPOList");
                //调用 获取服务器返回结果
                return marketCalendarRequest(baseRequest.getBase(), ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map).getList();
            case "4":
                if (!VersionUtil.geTargetVersion(baseRequest.getBase().getAppVersion(), "5.3.0")) {
                    map = getParamsCalendar(params, "shareholdersMeetingList");
                    //调用 获取服务器返回结果
                    return marketCalendarRequest(baseRequest.getBase(), ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map).getList();
                } else {
                    Map<String, Object> shareData = new HashMap<>();
                    shareData.put("pageHelp.pageNo", data.get("pageNo"));
                    shareData.put("pageHelp.pageSize", data.get("pageSize"));
                    shareData.put("pageHelp.beginPage", data.get("pageNo"));
                    shareData.put("pageHelp.endPage", data.get("pageNo"));
                    shareData.put("pageHelp.cacheSize", "1");
                    shareData.put("isPagination", "true");
                    shareData.put("conveneDate" ,baseRequest.getReqContent().getTradeBeginDate());
                    shareData.put("sqlId", "COMMON_SSE_SCFW_TZZFW_GDDHWLTPZL_L");
                    QueryResponse<ShareholdersMeetingListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, shareData, baseRequest.getBase(), new TypeReference<QueryResponse<ShareholdersMeetingListResBody>>() {
                    });
                    List<MarketCalendarResBody> marketCalendarResBodies = new ArrayList<>();
                    if (result.getResult().size() > 0) {
                        for (ShareholdersMeetingListResBody meetingListResBody : result.getResult()) {
                            MarketCalendarResBody marketCalendarResBody = new MarketCalendarResBody();
                            marketCalendarResBody.setStockCode(meetingListResBody.getSecCode());
                            marketCalendarResBody.setStockAbbr(meetingListResBody.getSecNameCn());
                            marketCalendarResBody.setTradeBeginDate(meetingListResBody.getConveneDate());
                            marketCalendarResBody.setTradeEndDate(meetingListResBody.getConveneDate());
                            marketCalendarResBodies.add(marketCalendarResBody);
                        }
                    }
                    //调用 获取服务器返回结果
                    return marketCalendarResBodies;
                }
            case "5":
                map = getParamsCalendar(params, "eInterviewList");
                //调用 获取服务器返回结果
                return marketCalendarRequest(baseRequest.getBase(), ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map).getList();
            case "6":
                map = getParamsCalendar(params, "roadshowList");
                //调用 获取服务器返回结果
                return marketCalendarRequest(baseRequest.getBase(), ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map).getList();
            case "8":
                map = getParamsCalendar(params, "reportAppointment");
                //调用 获取服务器返回结果
                return marketCalendarRequest(baseRequest.getBase(), ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map).getList();
            case "9":
                map = getParamsCalendar(params, "activityInfo");
                //调用 获取服务器返回结果
                return marketCalendarRequest(baseRequest.getBase(), ApiCodeConstants.SYS_PROXY_CODE_MARKET_CALENDAR_LIST, map).getList();
            default:
                break;
        }
        return null;
    }


    private Map<String, Object> getParamsCalendar(Map<String, Object> params, String type) {
        switch (type) {
            case "suspensionList":
                params.put("order", "ext1|asc,stockCode|asc,tradeBeginDate|asc");
                params.put("bizType", "7");
                return params;
            case "bonusDividendExList":
                params.put("order", "stockCode");
                params.put("bizType", "5");
                return params;
            case "bonusList":
                params.put("order", "ext1|asc,stockCode|asc,tradeBeginDate|asc");
                params.put("bizType", "6");
                return params;
            case "IPOList":
            case "IPOListTwo":
                params.put("order", "stockCode|asc,ext1|asc");
                params.put("bizType", "1");
                return params;
            case "shareholdersMeetingList":
                params.put("order", "stockCode");
                params.put("bizType", "4");
                return params;
            case "eInterviewList":
                params.put("order", "stockCode");
                params.put("bizType", "3");
                return params;
            case "roadshowList":
                params.put("order", "tradeBeginDate|desc,stockCode|desc");
                params.put("bizType", "2");
                return params;
            case "reportAppointment":
                params.put("order", "ext1|asc,stockCode|asc,tradeBeginDate|asc");
                params.put("bizType", "8");
                return params;
            case "activityInfo":
                params.put("order", "ext1|asc,stockCode|asc,tradeBeginDate|asc");
                params.put("bizType", "9");
                return params;
            default:
                break;
        }
        return params;
    }

    /**
     * 获取我的股票自选
     */
    private String getStockCode(BaseRequest<? extends ReqContentVO> baseRequest) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        data.put("uid", baseRequest.getBase().getUid());
        data.put("token", "APPMQUERY");
        data.put("trademarket", "SH");
        SysProxyConfig info = sysProxyFeign.getInfo(ApiCodeConstants.SYS_PROXY_CODE_OPTIONAL_STOCK_LIST);
        ProxyConfig proxyConfig = new ProxyConfig();
        BeanUtil.copyProperties(info, proxyConfig);
        proxyConfig.setData(data);
        proxyConfig.setBase(baseRequest.getBase());
        OptionalstockListDto result = proxyProvider.proxy(proxyConfig, new TypeReference<OptionalstockListDto>() {
        });
        //获取失败
        if (!ObjectUtil.equal(result.getStatus(), "1")) {
            throw new AppException(result.getReason());
        }
        //开始拼接字符串
        for (OptionalstockListDto.FollowCompaniesDTO followCompany : result.getFollowCompanies()) {
            if (followCompany.getStockCode().length() < 7) {
                sb.append(followCompany.getStockCode()).append(",");
            }
        }
        //如果拼接字符串出现如   1|23|45|666   ,最后一个有,，去除这个,
        if (StrUtil.isNotBlank(sb) && ObjectUtil.equals(sb.lastIndexOf(","), sb.length() - 1)) {
            sb.replace(sb.length() - 1, sb.length(), "");
        }
        return sb.toString();
    }

    public List<MarketCalendarResBody> addType(List<MarketCalendarResBody> list) {
        // 6开头 -> EQU+ASH
        // 9开头 -> EQU+BSH
        String roadShowUrl = sysConfigFeign.getConfigKey(AppConstants.SYS_CONFIG_KEY_RoadShowUrl);
        String code = "";
        String bizType = "";
        for (MarketCalendarResBody ipoListResBody : list) {
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
        return list;
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

    public List<GetTradeDayResBody> getTradeDay(BaseRequest<GetTradeDayReqBody> baseRequest) {
        baseRequest.getReqContent().setTday(DateUtil.format(new Date(), "yyyyMMdd"));
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        SoaResponse<GetTradeDayResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_TRADE_DAY, data, baseRequest.getBase(), new TypeReference<SoaResponse<GetTradeDayResBody>>() {
        });
        if (!Objects.equals("999999", result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return result.getList();
    }

}
