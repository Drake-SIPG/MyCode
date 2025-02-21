package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.MarketDailyTransactionOverviewResBody;
import com.sse.sseapp.form.response.MarketTotalInfoDetailResBody;
import com.sse.sseapp.form.response.MarketTotalInfoResBody;
import com.sse.sseapp.proxy.query.dto.HKStockConnectDto;
import com.sse.sseapp.service.MarketOverviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 市场概览
 */
@RestController
@RequestMapping("/marketOverview")
@Slf4j
public class MarketOverviewController extends BaseController {
    @Autowired
    MarketOverviewService marketOverviewService;

    /**
     * 市场概览-市场总貌
     */
    @PostMapping("/totalInfo")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("市场概览-市场总貌")
    public RespBean<MarketTotalInfoResBody> totalInfo(@RequestBody BaseRequest<MarketTotalInfoReqBody> reqBody) {
        return success(marketOverviewService.totalInfo(reqBody));
    }

    /**
     * 市场概览-市场总貌详情
     */
    @PostMapping("/totalInfoDetail")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("市场概览-市场总貌详情")
    public RespBean<MarketTotalInfoDetailResBody> totalInfoDetail(@RequestBody BaseRequest<MarketTotalInfoDetailReqBody> reqBody) {
        return success(marketOverviewService.totalInfoDetail(reqBody));
    }

    /**
     * 市场概览-每日成交概况hkStockConnect
     */
    @PostMapping("/dailyTransactionOverview")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("市场概览-每日成交概况")
    public RespBean<MarketDailyTransactionOverviewResBody> dailyTransactionOverview(@RequestBody BaseRequest<MarketDailyTransactionOverviewReqBody> reqBody) {
        return success(marketOverviewService.dailyTransactionOverview(reqBody));
    }

    /**
     * 市场概览-港股通
     */
    @PostMapping("/hkStockConnect")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("市场概览-港股通")
    public RespBean<HKStockConnectDto> hkStockConnect(@RequestBody BaseRequest<MarketHkStockConnectReqBody> marketHkStockConnectReqBodyBaseRequest) {
        return success(marketOverviewService.hkStockConnect(marketHkStockConnectReqBodyBaseRequest));
    }

    /**
     * 市场概览-港股通页面重构
     */
    @PostMapping("/hkStock")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("市场概览-港股通页面重构")
    public RespBean<?> hkStock() {
        return RespBean.success(marketOverviewService.hkStock());
    }


}
