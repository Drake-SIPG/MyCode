package com.sse.sseapp.controller.system;

import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProjectKCBTBSubscription;
import com.sse.sseapp.domain.system.SysProjectSubscription;
import com.sse.sseapp.service.system.SysProjectKCBTBSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 14:37
 */
@RestController
@RequestMapping("/dataStore/system/KCBTBSubscription")
public class SysProjectKCBTBSubscriptionController extends BaseController {
    @Autowired
    SysProjectKCBTBSubscriptionService sysProjectKCBTBSubscriptionService;

    @PostMapping("/getSubscribeList")
    public AjaxResult getSubscribeList(@RequestBody SysProjectKCBTBSubscription subscription){
        return success(sysProjectKCBTBSubscriptionService.getSubscribeList(subscription));
    }

    @PostMapping("/subscribeProject")
    public AjaxResult subscribeProject(@RequestBody SysProjectKCBTBSubscription subscribe){
        sysProjectKCBTBSubscriptionService.subscribeProject(subscribe);
        return success();
    }

    @PostMapping("/unSubscribeProject")
    public AjaxResult unSubscribeProject(@RequestBody SysProjectKCBTBSubscription subscribe){
        sysProjectKCBTBSubscriptionService.unSubscribeProject(subscribe);
        return success();
    }
}
