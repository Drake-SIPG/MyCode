package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.StockKLineReqBody;
import com.sse.sseapp.form.request.StockLineReqBody;
import com.sse.sseapp.form.request.StockSnapReqBody;
import com.sse.sseapp.service.StockDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 行情数据获取
 */
@RestController
@RequestMapping("/stockLine")
@Slf4j
public class StockLineController extends BaseController {

    @Autowired
    private StockDataService service;

    /**
     * 获取k线数据
     * @return 绘制数据
     */
    @PostMapping("/getKLineData")
    @Log("获取K线数据")
    @Decrypt
    @Encrypt
    public RespBean<?> getKLineData(@RequestBody BaseRequest<StockKLineReqBody> reqBody){
        return success(service.getKLineData(reqBody));
    }

    /**
     * 获取快照数据
     * @return 快照数据
     */
    @PostMapping("/getSnapData")
    @Log("获取快照数据")
    @Decrypt
    @Encrypt
    public RespBean<?> getSnapData(@RequestBody BaseRequest<StockSnapReqBody> reqBody){
        return success(service.getSnapData(reqBody));
    }

    /**
     * 获取当天走势数据
     * @return 当天走势数据
     */
    @PostMapping("/getLineData")
    @Log("获取当天走势数据")
    @Decrypt
    @Encrypt
    public RespBean<?> getLineData(@RequestBody BaseRequest<StockLineReqBody> reqBody){
        return success(service.getLineData(reqBody));
    }
}
