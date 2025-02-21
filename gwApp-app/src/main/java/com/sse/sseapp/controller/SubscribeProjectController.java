package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.SubscribeProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/27 11:47
 */
@RestController
@RequestMapping("/subscribeProject")
@Slf4j
public class SubscribeProjectController extends BaseController {

    @Autowired
    SubscribeProjectService service;

    /**
     * Merge项目订阅
     */
    @PostMapping("/mergeSubscribeProject")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("Merge项目订阅")
    public RespBean<?> mergeSubscribeProject(@RequestBody BaseRequest<MergeSubscribeProjectReqBody> reqBody) {
        service.mergeSubscribeProject(reqBody);
        return RespBean.success();
    }

    /**
     * Register项目订阅
     */
    @PostMapping("/registerSubscribeProject")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("Register项目订阅")
    public RespBean<?> registerSubscribeProject(@RequestBody BaseRequest<RegisterSubscribeProjectReqBody> reqBody) {
        service.registerSubscribeProject(reqBody);
        return RespBean.success();
    }

    /**
     * refinancing项目订阅
     */
    @PostMapping("/refinancingSubscribeProject")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("refinancing项目订阅")
    public RespBean<?> refinancingSubscribeProject(@RequestBody BaseRequest<RefinancingSubscribeProjectReqBody> reqBody) {
        service.refinancingSubscribeProject(reqBody);
        return RespBean.success();
    }

    /**
     * 科创版转版项目订阅
     */
    @PostMapping("/switchBoardSubscribeProject")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("科创版转版项目订阅")
    public RespBean<?> switchBoardSubscribeProject(@RequestBody BaseRequest<SwitchBoardSubscribeProjectReqBody> reqBody) {
        service.switchBoardSubscribeProject(reqBody);
        return RespBean.success();
    }

    /**
     * dr项目订阅
     */
    @PostMapping("/drSubscribeProject")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("dr项目订阅")
    public RespBean<?> drSubscribeProject(@RequestBody BaseRequest<DrSubscribeProjectReqBody> reqBody) {
        service.drSubscribeProject(reqBody);
        return RespBean.success();
    }
}
