package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.AppOneBusinessPushReqBody;
import com.sse.sseapp.form.request.AppOneUserRecordReqBody;
import com.sse.sseapp.form.request.AppOneUserUsedReqBody;
import com.sse.sseapp.service.AppOneBusinessPushService;
import com.sse.sseapp.service.AppOneUserRecordService;
import com.sse.sseapp.service.AppOneUserUsedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/investorProtal")
public class InvestorProtalController {

    @Autowired
    private AppOneUserRecordService appOneUserRecordService;

    @Autowired
    private AppOneUserUsedService appOneUserUsedService;

    @Autowired
    private AppOneBusinessPushService appOneBusinessPushService;

    /**
     * 保存用户选择
     */
    @PostMapping("/saveUserChoose")
    @ResponseBody
    @Log("保存用户选择")
    @Decrypt
    @Encrypt
    public RespBean<?> saveUserChoose(@Validated @RequestBody BaseRequest<AppOneUserRecordReqBody> request) {
        int row = this.appOneUserRecordService.saveOrUpdate(request);
        if (row > 0) {
            return RespBean.success();
        } else {
            return RespBean.error();
        }
    }

    /**
     * 查看用户选择业务类型
     */
    @PostMapping("/getUserChooseList")
    @ResponseBody
    @Log("查看用户选择业务类型")
    @Decrypt
    @Encrypt
    public RespBean<?> getUserChooseList(@RequestBody BaseRequest<AppOneUserRecordReqBody> request) {
        return this.appOneUserRecordService.userChooseList(request);
    }

    /**
     * 最近使用
     */
    @PostMapping("/getUserRecentUsedList")
    @ResponseBody
    @Log("最近使用")
    @Decrypt
    @Encrypt
    public RespBean<?> getUserRecentUsedList(@RequestBody BaseRequest<AppOneUserUsedReqBody> request) {
        return this.appOneUserUsedService.recentUsedList(request);
    }

    /**
     * 新增用户选择
     */
    @PostMapping("/addUsedNav")
    @ResponseBody
    @Log("新增用户选择")
    @Decrypt
    @Encrypt
    public RespBean<?> addUsedNav(@Validated @RequestBody BaseRequest<AppOneUserUsedReqBody> request) {
        int row = this.appOneUserUsedService.addNav(request);
        if (row > 0) {
            return RespBean.success();
        } else {
            return RespBean.error();
        }
    }


    /**
     * 新增业务更新推送记录
     */
    @PostMapping("/addBusinessPush")
    @ResponseBody
    @Log("新增业务更新推送记录")
    @Decrypt
    @Encrypt
    public RespBean<?> addBusinessPush(@Validated @RequestBody BaseRequest<AppOneBusinessPushReqBody> request) {
        int row = this.appOneBusinessPushService.addBusiness(request);
        if (row > 0) {
            return RespBean.success();
        } else {
            return RespBean.error();
        }
    }

    /**
     * 删除用户未读消息
     */
    @PostMapping("/delUserUnreadData")
    @ResponseBody
    @Log("删除用户未读消息")
    @Decrypt
    @Encrypt
    public RespBean<?> delUserUnreadData(@Validated @RequestBody BaseRequest<AppOneBusinessPushReqBody> request) {
        int row = this.appOneBusinessPushService.delUserUnreadData(request);
        if (row > 0) {
            return RespBean.success();
        } else {
            return RespBean.error();
        }
    }

    /**
     * 查看用户未读业务列表
     */
    @PostMapping("/getUserUnreadList")
    @ResponseBody
    @Log("查看用户未读业务列表")
    @Decrypt
    @Encrypt
    public RespBean<?> getUserUnreadList(@RequestBody BaseRequest<AppOneBusinessPushReqBody> request) {
        return this.appOneBusinessPushService.getUserUnreadList(request);
    }

}
