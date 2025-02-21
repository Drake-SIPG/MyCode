package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.IndexTrendReqBody;
import com.sse.sseapp.form.request.YieldCurveReqBody;
import com.sse.sseapp.service.BenchmarkIndicatorsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基准指标
 *
 * @author wy
 * @date 2023-08-09
 */
@RestController
@RequestMapping("/benchmarkIndicators")
@Slf4j
public class BenchmarkIndicatorsController {

    @Autowired
    private BenchmarkIndicatorsService benchmarkIndicatorsService;

    /**
     * 指数走势概况
     */
    @PostMapping("/indexTrendInfo")
    @Log("指数走势概况")
    @Decrypt
    @Encrypt
    public RespBean<?> indexTrendInfo(@RequestBody BaseRequest<IndexTrendReqBody> body) {
        return this.benchmarkIndicatorsService.indexTrendInfo(body);
    }

    /**
     * 指数走势
     */
    @PostMapping("/indexTrend")
    @Log("指数走势")
    @Decrypt
    @Encrypt
    public RespBean<?> indexTrend(@RequestBody BaseRequest<IndexTrendReqBody> body) {
        return this.benchmarkIndicatorsService.indexTrend(body);
    }

    /**
     * 收益率曲线
     */
    @PostMapping("/yieldCurve")
    @Log("收益率曲线")
    @Decrypt
    @Encrypt
    public RespBean<?> yieldCurve(@RequestBody BaseRequest<YieldCurveReqBody> body) {
        return this.benchmarkIndicatorsService.yieldCurve(body);
    }

}
