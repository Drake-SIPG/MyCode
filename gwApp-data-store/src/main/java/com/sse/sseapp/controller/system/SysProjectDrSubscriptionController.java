package com.sse.sseapp.controller.system;

import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProjectDrSubscription;
import com.sse.sseapp.service.system.SysProjectDrSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: liangjm
 * @create-date: 2023/12/28 15:20
 */
@RestController
@RequestMapping("/dataStore/system/DrSubscription")
public class SysProjectDrSubscriptionController extends BaseController{
    @Autowired
    SysProjectDrSubscriptionService subscriptionService;

    @PostMapping("/getSubscribeList")
    public AjaxResult getSubscribeList(@RequestBody SysProjectDrSubscription subscription){
        return success(subscriptionService.getSubscribeList(subscription));
    }

    @PostMapping("/subscribeProject")
    public AjaxResult subscribeProject(@RequestBody SysProjectDrSubscription subscribe){
        subscriptionService.subscribeProject(subscribe);
        return success();
    }

    @PostMapping("/unSubscribeProject")
    public AjaxResult unSubscribeProject(@RequestBody SysProjectDrSubscription subscribe){
        subscriptionService.unSubscribeProject(subscribe);
        return success();
    }

    @GetMapping("/geUserNameByProId")
    public List<String> geUserNameByProId(@RequestParam("proId") String proId){
        return subscriptionService.geUserNameByProId(proId);
    }

}
