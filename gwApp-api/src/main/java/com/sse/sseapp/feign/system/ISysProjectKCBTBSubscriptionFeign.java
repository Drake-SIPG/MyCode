package com.sse.sseapp.feign.system;


import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProjectKCBTBSubscription;
import com.sse.sseapp.domain.system.SysProjectSubscription;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/KCBTBSubscription")
public interface ISysProjectKCBTBSubscriptionFeign {

    /**
     *  获取订阅时间
     */
    @PostMapping("/getSubscribeList")
    AjaxResult getSubscribeList(@RequestBody SysProjectKCBTBSubscription subscription);

    @PostMapping("/subscribeProject")
    AjaxResult subscribeProject(@RequestBody SysProjectKCBTBSubscription subscribe);

    @PostMapping("/unSubscribeProject")
    AjaxResult unSubscribeProject(@RequestBody SysProjectKCBTBSubscription subscribe);
}
