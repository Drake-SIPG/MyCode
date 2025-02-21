package com.sse.sseapp.controller;

import com.sse.sseapp.core.annotation.Log;
import com.sse.sseapp.core.enums.BusinessType;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.push.AppPushMessage;
import com.sse.sseapp.service.AppPushMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息推送
 *
 * @author wy
 * @date 2023-07-20
 */
@RestController
@RequestMapping("/appPushMessage")
public class AppPushMessageController {

    @Autowired
    private AppPushMessageService appPushMessageService;

    /**
     * 新增消息推送
     *
     * @param appPushMessage 请求参数
     */
    @Log(title = "消息推送", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody AppPushMessage appPushMessage) {
        return this.appPushMessageService.add(appPushMessage);
    }

}
