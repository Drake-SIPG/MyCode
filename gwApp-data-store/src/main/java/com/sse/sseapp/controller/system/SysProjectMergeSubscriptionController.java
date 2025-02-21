package com.sse.sseapp.controller.system;

import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProjectMergeSubscription;
import com.sse.sseapp.service.system.SysProjectMergeSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 15:20
 */
@RestController
@RequestMapping("/dataStore/system/MergeSubscription")
public class SysProjectMergeSubscriptionController extends BaseController{
    @Autowired
    SysProjectMergeSubscriptionService subscriptionService;

    @PostMapping("/getSubscribeList")
    public AjaxResult getSubscribeList(@RequestBody SysProjectMergeSubscription subscription){
        return success(subscriptionService.getSubscribeList(subscription));
    }

    @PostMapping("/subscribeProject")
    public AjaxResult subscribeProject(@RequestBody SysProjectMergeSubscription subscribe){
        subscriptionService.subscribeProject(subscribe);
        return success();
    }

    @PostMapping("/unSubscribeProject")
    public AjaxResult unSubscribeProject(@RequestBody SysProjectMergeSubscription subscribe){
        subscriptionService.unSubscribeProject(subscribe);
        return success();
    }
}
