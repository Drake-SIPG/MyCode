package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;

import com.sse.sseapp.domain.system.SysProjectMergeSubscription;
import com.sse.sseapp.domain.system.SysProjectSubscription;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/subscription")
public interface ISysProjectSubscriptionFeign {
    @PostMapping("/getSubscribeList")
    AjaxResult getSubscribeList(@RequestBody SysProjectSubscription subscription);

    @PostMapping("/getSubscribeListNew")
    List<SysProjectSubscription> getSubscribeListNew(@RequestBody SysProjectSubscription subscription);

    @PostMapping("/subscribeProject")
    AjaxResult subscribeProject(@RequestBody SysProjectSubscription subscribe);

    @PostMapping("/unSubscribeProject")
    AjaxResult unSubscribeProject(@RequestBody SysProjectSubscription subscribe);

    @GetMapping("/geUserNameByProId")
    List<String> geUserNameByProId(@RequestParam("proId") String proId);
}
