package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.CompanyProfileReqBody;
import com.sse.sseapp.form.request.GetPromiseListReqBody;
import com.sse.sseapp.form.request.ProMiShoReqBody;
import com.sse.sseapp.service.StockDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 股票行情相关接口
 */
@RestController
@RequestMapping("/stockDetail")
@Slf4j
public class StockDetailController extends BaseController {

    @Autowired
    private StockDetailService service;

    /**
     * 公司概况
     */
    @PostMapping("/getCompanyProfileList")
    @Log("公司概况接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getCompanyProfileList(@RequestBody BaseRequest<CompanyProfileReqBody> body) {
        return this.service.getCompanyProfileList(body);
    }

    /**
     * 高管人员基础信息接口
     */
    @PostMapping("/getSeniorManagersList")
    @Log("高管人员基础信息接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getSeniorManagersList(@RequestBody BaseRequest<CompanyProfileReqBody> body) {
        return this.service.getSeniorManagersList(body);
    }

    /**
     * 持股变动接口
     */
    @PostMapping("/getStockHoldingChangeList")
    @Log("持股变动接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getStockHoldingChangeList(@RequestBody BaseRequest<CompanyProfileReqBody> body) {
        return this.service.getStockHoldingChangeList(body);
    }

    /**
     * 承诺履行接口
     */
    @PostMapping("/getProMiShoList")
    @Log("承诺履行接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getProMiShoList(@RequestBody BaseRequest<ProMiShoReqBody> body) {
        return this.service.getProMiShoList(body);
    }

    /**
     * 行情股票详情承诺履行接口
     */
    @PostMapping("/getPromiseList")
    @Log("行情股票详情承诺履行接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getPromiseList(@RequestBody BaseRequest<GetPromiseListReqBody> body) {
        return RespBean.success(service.getPromiseList(body));
    }

}
