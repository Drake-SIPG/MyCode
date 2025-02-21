package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.GetPhDataByUserReqBody;
import com.sse.sseapp.form.request.GetZqDataByUserReqBody;
import com.sse.sseapp.service.MyNewStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/10 9:50
 */
@RestController
@RequestMapping("/myNewStock")
@Slf4j
public class MyNewStockController extends BaseController {
    @Autowired
    MyNewStockService myNewStockService;

    /**
     * 我的新股-配号查询接口
     */
    @PostMapping("/getPhDataByUser")
    @ResponseBody
    @Log("配号查询")
    @Decrypt
    @Encrypt
    public RespBean<?> getPhDataByUser(@RequestBody BaseRequest<GetPhDataByUserReqBody> getPhDataByUserReqBody){
        return RespBean.success(myNewStockService.getPhDataByUser(getPhDataByUserReqBody));
    }

    /**
     * 我的新股-中签查询接口
     */
    @PostMapping("/getZqDataByUser")
    @ResponseBody
    @Log("中签查询")
    @Decrypt
    @Encrypt
    public RespBean<?> getZqDataByUser(@RequestBody BaseRequest<GetZqDataByUserReqBody> getZqDataByUserReqBody){
        return RespBean.success(myNewStockService.getZqDataByUser(getZqDataByUserReqBody));
    }
}
