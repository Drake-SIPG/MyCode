package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.soa.dto.QuatAbelDto;
import com.sse.sseapp.service.QuotesNewService;
import com.sse.sseapp.service.QuotesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 行情
 */
@RestController
@RequestMapping("/quotes")
@Slf4j
public class QuotesController extends BaseController {
    @Autowired
    QuotesService quotesService;

    @Autowired
    QuotesNewService quotesNewService;

//    /**
//     * 行情信息
//     */
//    @PostMapping("/info")
//    @ResponseBody
//    @Decrypt
//    @Encrypt
//    @Log("行情-行情信息")
//    public RespBean<QuotesInfoResBody> info(@RequestBody BaseRequest<QuotesInfoReqBody> reqBody) {
//        return success(quotesNewService.info(reqBody));
//    }
//
//    /**
//     * 行情-期权列表
//     */
//    @PostMapping("/optionList")
//    @ResponseBody
//    @Decrypt
//    @Encrypt
//    @Log("行情-期权列表")
//    public RespBean<OptionListResBody> optionList(@RequestBody BaseRequest<OptionListReqBody> reqBody) {
//        return success(quotesNewService.optionList(reqBody));
//    }
//
//    /**
//     * 行情-期权详情
//     */
//    @PostMapping("/optionDetail")
//    @ResponseBody
//    @Decrypt
//    @Encrypt
//    @Log("行情-期权详情")
//    public RespBean<OptionDetailResBody> optionDetail(@RequestBody BaseRequest<OptionDetailReqBody> reqBody) {
//        return success(quotesNewService.optionDetail(reqBody));
//    }
//
//    /**
//     * 行情-总览
//     */
//    @PostMapping("/overview")
//    @ResponseBody
//    @Decrypt
//    @Encrypt
//    @Log("行情-总览")
//    public RespBean<?> overview(@RequestBody BaseRequest<OptionOverviewReqBody> reqBody) {
//        if (reqBody.getReqContent().getCode().length() > 6) {
//            return success(quotesNewService.optionOverview(reqBody));
//        } else {
//            return success(quotesNewService.overview(reqBody));
//        }
//    }

    /**
     * 行情-成交概况
     */
    @PostMapping("/quatAbel")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("行情-成交概况")
    public RespBean<QuatAbelDto.QuatAbelBean> quatAbel(@RequestBody BaseRequest<OptionQuatAbelReqBody> reqBody) {
        return success(quotesNewService.quatAbel(reqBody));
    }

//    /**
//     * 行情-五档
//     */
//    @PostMapping("/bestFive")
//    @ResponseBody
//    @Decrypt
//    @Encrypt
//    @Log("行情-五档")
//    public RespBean<OptionBestFiveResBody> bestFive(@RequestBody BaseRequest<OptionBestFiveReqBody> reqBody) {
//        return success(quotesNewService.bestFive(reqBody));
//    }
//
//    /**
//     * 行情-明细
//     */
    @PostMapping("/details")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("行情-明细")
    public RespBean<QuotesDetailsResBody> details(@RequestBody BaseRequest<QuotesDetailsReqBody> reqBody) {
        return success(quotesNewService.details(reqBody));
    }
//
//    /**
//     * 行情-分价
//     */
//    @PostMapping("/trd2")
//    @ResponseBody
//    @Decrypt
//    @Encrypt
//    @Log("行情-分价")
//    public RespBean<OptionTrd2ResBody> trd2(@RequestBody BaseRequest<OptionTrd2ReqBody> reqBody) {
//        return success(quotesNewService.trd2(reqBody));
//    }
}
