package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.KcbJygkListReqBody;
import com.sse.sseapp.form.request.KcbZlpsListReqBody;
import com.sse.sseapp.form.request.MarketDataReqBody;
import com.sse.sseapp.service.StockTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stockTrade")
@Slf4j
public class StockTradeController extends BaseController {

    @Autowired
    private StockTradeService stockTradeService;

    /**
     * 股票市场总貌统计
     */
    @PostMapping("/getMarketData")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("股票市场总貌统计")
    public RespBean<?> getMarketData(@RequestBody BaseRequest<MarketDataReqBody> reqBody) {
        return this.stockTradeService.getMarketData(reqBody);
    }

    /**
     * 获取科创板战略配售列表
     */
    @PostMapping("/getKcbZlpsList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("获取科创板战略配售列表")
    public RespBean<?> getKcbZlpsList(@RequestBody BaseRequest<KcbZlpsListReqBody> reqBody) {
        return this.stockTradeService.getKcbZlpsList(reqBody);
    }

    /**
     * 科创板交易公开信息、严重异常波动数据列表
     */
    @PostMapping("/getKcbJygkList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("科创板交易公开信息、严重异常波动数据列表")
    public RespBean<?> getKcbJygkList(@RequestBody BaseRequest<KcbJygkListReqBody> reqBody) {
        return this.stockTradeService.getKcbJygkList(reqBody);
    }


}
