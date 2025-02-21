package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.UnSubscribeProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/27 15:02
 */
@RestController
@RequestMapping("/unSubscribeProject")
@Slf4j
public class UnSubscribeProjectController extends BaseController {

    @Autowired
    UnSubscribeProjectService service;

    /**
     * Merge项目取消订阅
     */
    @PostMapping("/unMergeSubscribeProject")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("Merge项目取消订阅")
    public RespBean<?> unMergeSubscribeProject(@RequestBody BaseRequest<UnMergeSubscribeProjectReqBody> reqBody) {
        service.unMergeSubscribeProject(reqBody);
        return RespBean.success();
    }

    /**
     * Register项目取消订阅
     */
    @PostMapping("/unRegisterSubscribeProject")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("Register项目取消订阅")
    public RespBean<?> unRegisterSubscribeProject(@RequestBody BaseRequest<UnRegisterSubscribeProjectReqBody> reqBody) {
        service.unRegisterSubscribeProject(reqBody);
        return RespBean.success();
    }

    /**
     * refinancing项目取消订阅
     */
    @PostMapping("/unRefinancingSubscribeProject")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("refinancing项目取消订阅")
    public RespBean<?> unRefinancingSubscribeProject(@RequestBody BaseRequest<UnRefinancingSubscribeProjectReqBody> reqBody) {
        service.unRefinancingSubscribeProject(reqBody);
        return RespBean.success();
    }

    /**
     * 科创版转版项目取消订阅
     */
    @PostMapping("/unSwitchBoardSubscribeProject")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("科创版转版项目取消订阅")
    public RespBean<?> unSwitchBoardSubscribeProject(@RequestBody BaseRequest<UnSwitchBoardSubscribeProjectReqBody> reqBody) {
        service.unSwitchBoardSubscribeProject(reqBody);
        return RespBean.success();
    }

    /**
     * Dr取消订阅
     */
    @PostMapping("/unDrSubscribeProject")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("dr项目取消订阅")
    public RespBean<?> unDrSubscribeProject(@RequestBody BaseRequest<UnDrSubscribeProjectReqBody> reqBody) {
        service.unDrSubscribeProject(reqBody);
        return RespBean.success();
    }
}
