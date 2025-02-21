package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.MarketTotalInfoResBody;
import com.sse.sseapp.service.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 日期 接口
 *
 * @author zhengyaosheng
 * @date 2023-04-03
 */
@RestController
@Slf4j
@RequestMapping("/calendar")
public class CalendarController extends BaseController {

    @Autowired
    private CalendarService calendarService;

    /**
     * 日历
     */
    @PostMapping("/calendarList")
    @Log("日历")
    @Decrypt
    @Encrypt
    public RespBean<?> calendarList(@RequestBody BaseRequest<CalendarListReqBody> body) {
        return this.calendarService.calendarList(body);
    }

    /**
     * 日历-交易日获取
     */
    @PostMapping("/getCalendarDayListByDateRange")
    @Log("日历-交易日获取")
    @Decrypt
    @Encrypt
    public RespBean<?> getCalendarDayListByDateRange(@RequestBody BaseRequest<CalendarDayListReqBody> body) {
        return RespBean.success(this.calendarService.getCalendarDayListByDateRange(body));
    }

    /**
     * 行情-详情页面-日历(股东大会)
     */
    @PostMapping("/shareholdersMeetingList")
    @Log("行情-详情页面-日历(股东大会)")
    @Decrypt
    @Encrypt
    public RespBean<?> shareholdersMeetingList(@RequestBody BaseRequest<ShareholdersMeetingListReqBody> calendarListDtoReqBod){
        return RespBean.success(this.calendarService.shareholdersMeetingList(calendarListDtoReqBod));
    }

    /**
     * 行情-详情页面-大宗
     */
    @PostMapping("/getBlockTradeData")
    @Log("行情-详情页面-大宗")
    @Decrypt
    @Encrypt
    public RespBean<?> getBlockTradeData(@RequestBody BaseRequest<GetBlockTradeDataReqBody> getBlockTradeDataReqBody){
        return RespBean.success(this.calendarService.getBlockTradeData(getBlockTradeDataReqBody));
    }

    /**
     * 获取MarketCalendarList信息
     */
    @PostMapping("/getMarketCalendarList")
    @Log("获取MarketCalendarList信息")
    @Decrypt
    @Encrypt
    public RespBean<?> getMarketCalendarList(@RequestBody BaseRequest<MarketCalendarListReqBody> baseRequest){
        return RespBean.success(this.calendarService.getMarketCalendarList(baseRequest));
    }

    /**
     * 获取上一交易日接口
     */
    @PostMapping("/getTradeDay")
    @Log("获取上一交易日接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getTradeDay(@RequestBody BaseRequest<GetTradeDayReqBody> baseRequest){
        return RespBean.success(this.calendarService.getTradeDay(baseRequest));
    }

}
