package com.sse.sseapp.feign.app;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.app.AppBondSubscription;
import com.sse.sseapp.domain.app.AppBondSubscriptionPo;
import com.sse.sseapp.domain.app.AppMessageDevice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "gwApp-data-store", path = "/dataStore/app/bondSubscription")
public interface IAppBondSubscriptionFeign {

    /**
     * 根据用户ID查询用户订阅
     * @param userId
     * @return
     */
    @GetMapping("/getUserBondSubscription/{userId}")
    public List<String> getUserBondSubscription(@PathVariable(value = "userId") String userId);

    /**
     * 新增用户订阅
     * @param appBondSubscription
     * @return
     */
    @PostMapping("/addUserBondSubscription")
    public AjaxResult addUserBondSubscription(@RequestBody AppBondSubscription appBondSubscription);

    /**
     * 取消用户订阅
     * @param appBondSubscription
     * @return
     */
    @PostMapping("/cancelBondSubscription")
    public AjaxResult cancelBondSubscription(@RequestBody AppBondSubscriptionPo appBondSubscription);

    /**
     * 根据股票代码查询订阅用户手机号
     * @param bondCode
     * @return
     */
    @GetMapping("/getMobile/{bondCode}")
    public List<String> getMobile(@PathVariable(value = "bondCode") String bondCode);

}
