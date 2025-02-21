package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.RiskStockListReqBody;
import com.sse.sseapp.form.request.RiskTipsOfStarMarketReqBody;
import com.sse.sseapp.service.RiskPlateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * riskPlate 接口
 *
 * @author zhengyaosheng
 * @date 2023-05-24
 */
@RestController
@Slf4j
@RequestMapping("/riskPlate")
public class RiskPlateController {

    @Autowired
    private RiskPlateService riskPlateService;

    /**
     * 获取风险股票列表
     */
    @PostMapping("/getRiskStockList")
    @Log("获取风险股票列表")
    @Decrypt
    @Encrypt
    public RespBean<?> getRiskStockList(@RequestBody BaseRequest<RiskStockListReqBody> body) {
        return this.riskPlateService.getRiskStockList(body);
    }

    /**
     * 科创板风险警示、退市整理股票接口
     */
    @PostMapping("/getRiskTipsOfStarMarket")
    @Log("科创板风险警示、退市整理股票接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getRiskTipsOfStarMarket(@RequestBody BaseRequest<RiskTipsOfStarMarketReqBody> body) {
        return this.riskPlateService.getRiskTipsOfStarMarket(body);
    }

}
