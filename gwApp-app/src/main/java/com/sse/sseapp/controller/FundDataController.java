package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.FundDataService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/fundData")
public class FundDataController extends BaseController {
    @Autowired
    private FundDataService service;

    /**
     * 近期发行基金
     */
    @PostMapping("/recentlyReleased")
    @Log("近期发行基金")
    @Decrypt
    @Encrypt
    public RespBean<?> recentlyReleased(@RequestBody BaseRequest<RecentlyReleasedReqBody> body) {
        return RespBean.success(service.recentlyReleased(body).getResult());
    }

    /**
     * 近期上市基金
     */
    @PostMapping("/recentlyLaunched")
    @Log("近期上市基金")
    @Decrypt
    @Encrypt
    public RespBean<?> recentlyLaunched(@RequestBody BaseRequest<RecentlyLaunchedReqBody> body) {
        return RespBean.success(service.recentlyLaunched(body).getResult());
    }

    /**
     * 基金市场总览
     */
    @PostMapping("/marketOverview")
    @Log("基金市场总览")
    @Decrypt
    @Encrypt
    public RespBean<?> marketOverview(@RequestBody BaseRequest<MarketOverviewReqBody> body) {
        return RespBean.success(service.marketOverview(body).getResult());
    }

    /**
     * 基金列表-获取基金类型
     */
    @PostMapping("/getType")
    @Log("基金列表-获取基金类型")
    @Decrypt
    @Encrypt
    public RespBean<?> getType(@RequestBody BaseRequest<GetTypeReqBody> body) {
        return RespBean.success(service.getType(body).getResult());
    }

    /**
     * 基金列表-查询
     */
    @PostMapping("/query")
    @Log("基金列表-查询")
    @Decrypt
    @Encrypt
    public RespBean<?> query(@RequestBody BaseRequest<FundQueryReqBody> body) {
        return RespBean.success(service.query(body).getResult());
    }

    /**
     * 每日/月度统计-每日统计
     */
    @PostMapping("/dailyStatistics")
    @Log("每日/月度统计-每日统计")
    @Decrypt
    @Encrypt
    public RespBean<?> dailyStatistics(@RequestBody BaseRequest<DailyStatisticsReqBody> body) {
        return RespBean.success(service.dailyStatistics(body).getResult());
    }
}
