package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysProjectDrSubscription;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/DrSubscription")
public interface ISysProjectDrSubscriptionFeign {

    @PostMapping("/getSubscribeList")
    AjaxResult getSubscribeList(@RequestBody SysProjectDrSubscription subscription);

    @PostMapping("/subscribeProject")
    AjaxResult subscribeProject(@RequestBody SysProjectDrSubscription subscribe);

    @PostMapping("/unSubscribeProject")
    AjaxResult unSubscribeProject(@RequestBody SysProjectDrSubscription subscribe);

    @GetMapping("/geUserNameByProId")
    List<String> geUserNameByProId(@RequestParam("proId") String proId);
}
