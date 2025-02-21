package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;

import com.sse.sseapp.domain.system.SysProjectKCBTBSubscription;
import com.sse.sseapp.domain.system.SysProjectKCBZRZSubscription;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/KCBZRZSubscription")
public interface ISysProjectKCBZRZSubscriptionFeign {

    @PostMapping("/getSubscribeList")
    AjaxResult getSubscribeList(@RequestBody SysProjectKCBZRZSubscription subscription);

    @PostMapping("/subscribeProject")
    AjaxResult subscribeProject(@RequestBody SysProjectKCBZRZSubscription subscribe);

    @PostMapping("/unSubscribeProject")
    AjaxResult unSubscribeProject(@RequestBody SysProjectKCBZRZSubscription subscribe);
}
