package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProjectKCBZRZSubscription;
import com.sse.sseapp.domain.system.SysProjectMergeSubscription;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/MergeSubscription")
public interface ISysProjectMergeSubscriptionFeign {
    @PostMapping("/getSubscribeList")
    AjaxResult getSubscribeList(@RequestBody SysProjectMergeSubscription subscription);

    @PostMapping("/subscribeProject")
    AjaxResult subscribeProject(@RequestBody SysProjectMergeSubscription subscribe);

    @PostMapping("/unSubscribeProject")
    AjaxResult unSubscribeProject(@RequestBody SysProjectMergeSubscription subscribe);
}
