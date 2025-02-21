package com.sse.sseapp.controller.system;

import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProjectMergeSubscription;
import com.sse.sseapp.domain.system.SysProjectSubscription;
import com.sse.sseapp.service.system.SysProjectSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 15:22
 */
@RestController
@RequestMapping("/dataStore/system/subscription")
public class SysProjectSubscriptionController extends BaseController {
    @Autowired
    SysProjectSubscriptionService subscriptionService;

    @PostMapping("/getSubscribeList")
    public AjaxResult getSubscribeList(@RequestBody SysProjectSubscription subscription){
        return success(subscriptionService.getSubscribeList(subscription));
    }

    @PostMapping("/getSubscribeListNew")
    public List<SysProjectSubscription> getSubscribeListNew(@RequestBody SysProjectSubscription subscription){
        return subscriptionService.getSubscribeList(subscription);
    }

    @PostMapping("/subscribeProject")
    public AjaxResult subscribeProject(@RequestBody SysProjectSubscription subscribe){
        subscriptionService.subscribeProject(subscribe);
        return success();
    }

    @PostMapping("/unSubscribeProject")
    public AjaxResult unSubscribeProject(@RequestBody SysProjectSubscription subscribe){
        subscriptionService.unSubscribeProject(subscribe);
        return success();
    }

    @GetMapping("/geUserNameByProId")
    public List<String> geUserNameByProId(@RequestParam("proId") String proId){
        return subscriptionService.geUserNameByProId(proId);
    }
}
