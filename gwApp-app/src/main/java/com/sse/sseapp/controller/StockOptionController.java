package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.StockOptionDayReqBody;
import com.sse.sseapp.service.StockOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 股票期权 相关接口
 */
@RestController
@RequestMapping("/stockOption")
@Slf4j
public class StockOptionController extends BaseController {

    @Autowired
    private StockOptionService service;

    /**
     * 获取每日股票期权
     */
    @PostMapping("/getStockOptionDay")
    @Log("获取每日股票期权")
    @Decrypt
    @Encrypt
    public RespBean<?> getCompanyProfileList(@RequestBody BaseRequest<StockOptionDayReqBody> body) {
        return RespBean.success(this.service.getStockOptionDay(body));
    }

}
