package com.sse.sseapp.controller.app;


import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.app.AppBondSubscription;
import com.sse.sseapp.domain.app.AppBondSubscriptionPo;
import com.sse.sseapp.service.app.AppBondSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxinyu
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/dataStore/app/bondSubscription")
public class AppBondSubscriptionController extends BaseController {
    @Autowired
    private AppBondSubscriptionService service;

    @GetMapping("/getUserBondSubscription/{userId}")
    public List<String> getUserBondSubscription(@PathVariable(value = "userId") String userId) {
        return service.getUserBondSubscription(userId);
    }

    @GetMapping("/getMobile/{bondCode}")
    public List<String> getMobile(@PathVariable(value = "bondCode") String bondCode) {
        return service.getMobile(bondCode);
    }

    @PostMapping("/addUserBondSubscription")
    public AjaxResult addUserBondSubscription(@RequestBody AppBondSubscription appBondSubscription) {
        service.addUserBondSubscription(appBondSubscription);
        return success();
    }

    @PostMapping("/cancelBondSubscription")
    public AjaxResult cancelBondSubscription(@RequestBody AppBondSubscriptionPo appBondSubscription) {
        if (service.cancelBondSubscription(appBondSubscription)){
            return success();
        }else {
            return error();
        }
    }
}
