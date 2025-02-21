package com.sse.sseapp.controller.system;

import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProjectKCBTBSubscription;
import com.sse.sseapp.domain.system.SysProjectKCBZRZSubscription;
import com.sse.sseapp.service.system.SysProjectKCBZRZSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 15:17
 */
@RestController
@RequestMapping("/dataStore/system/KCBZRZSubscription")
public class SysProjectKCBZRZSubscriptionController extends BaseController {

    @Autowired
    SysProjectKCBZRZSubscriptionService subscriptionService;

    @PostMapping("/getSubscribeList")
    public AjaxResult getSubscribeList(@RequestBody SysProjectKCBZRZSubscription subscription){
        return success(subscriptionService.getSubscribeList(subscription));
    }

    @PostMapping("/subscribeProject")
    public AjaxResult subscribeProject(@RequestBody SysProjectKCBZRZSubscription subscribe){
        subscriptionService.subscribeProject(subscribe);
        return success();
    }

    @PostMapping("/unSubscribeProject")
    public AjaxResult unSubscribeProject(@RequestBody SysProjectKCBZRZSubscription subscribe){
        subscriptionService.unSubscribeProject(subscribe);
        return success();
    }

}
