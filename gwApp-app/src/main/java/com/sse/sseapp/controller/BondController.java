package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.BondService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bond")
@Slf4j
public class BondController extends BaseController {
    @Autowired
    private BondService service;

    /**
     *  业务提示栏目
     */
    @PostMapping("/businessPrompt")
    @ResponseBody
    @Log("业务提示栏目")
    @Decrypt
    @Encrypt
    public RespBean<?> businessPrompt(@RequestBody BaseRequest<BusinessPromptReqBody> request){
        return RespBean.success(service.businessPrompt(request).getResult());
    }

    /**
     *  债券基本要素
     */
    @PostMapping("/bondFundamentals")
    @ResponseBody
    @Log("债券基本要素")
    @Decrypt
    @Encrypt
    public RespBean<?> bondFundamentals(@RequestBody BaseRequest<BondFundamentalsReqBody> request){
        return RespBean.success(service.bondFundamentals(request).getResult());
    }

    /**
     *  查询用户订阅债券
     */
    @PostMapping("/getUserBondSubscription")
    @ResponseBody
    @Log("查询用户订阅债券")
    @Decrypt
    @Encrypt
    public RespBean<?> getUserBondSubscription(@RequestBody BaseRequest<GetUserBondSubscriptionReqBody> request){
        return RespBean.success(service.getUserBondSubscription(request));
    }

    /**
     *  查询用户是否订阅
     */
    @PostMapping("/isSubscription")
    @ResponseBody
    @Log("查询用户是否订阅")
    @Decrypt
    @Encrypt
    public RespBean<?> isSubscription(@RequestBody BaseRequest<IsSubscriptionReqBody> request){
        return RespBean.success(service.isSubscription(request));
    }

    /**
     *  新增用户订阅债券
     */
    @PostMapping("/addUserBondSubscription")
    @ResponseBody
    @Log("新增用户订阅债券")
    @Decrypt
    @Encrypt
    public RespBean<?> addUserBondSubscription(@RequestBody BaseRequest<AppBondSubscriptionReqBody> request){
        return service.addUserBondSubscription(request);
    }

    /**
     *  取消用户订阅债券
     */
    @PostMapping("/cancelBondSubscription")
    @ResponseBody
    @Log("取消用户订阅债券")
    @Decrypt
    @Encrypt
    public RespBean<?> cancelBondSubscription(@RequestBody BaseRequest<CancelBondSubscriptionReqBody> request){
        return service.cancelBondSubscription(request);
    }

}
