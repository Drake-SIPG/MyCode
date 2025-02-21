package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.HomePageIpoReqBody;
import com.sse.sseapp.form.request.IpoReqBody;
import com.sse.sseapp.service.IpoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * homeNew 接口
 *
 * @author zhengyaosheng
 * @date 2023-04-07
 */
@RestController
@Slf4j
@RequestMapping("/ipo")
public class IpoController extends BaseController {

    @Autowired
    private IpoService ipoService;

    /**
     * 获取ipo列表
     */
    @PostMapping("/IPOList")
    @Log("获取ipo列表")
    @Decrypt
    @Encrypt
    public RespBean<?> IPOList(@RequestBody BaseRequest<IpoReqBody> baseRequest) {
        return this.ipoService.IPOList(baseRequest);
    }

    /**
     * 获取ipo列表
     */
    @PostMapping("/newStockIPOList")
    @Log("获取ipo列表")
    @Decrypt
    @Encrypt
    public RespBean<?> newStockIPOList(@RequestBody BaseRequest<IpoReqBody> baseRequest) {
        return this.ipoService.newStockIPOList(baseRequest);
    }


    /**
     * 获取首页ipo列表
     */
    @PostMapping("/getHomePageIpo")
    @Log("获取首页ipo列表")
    @Decrypt
    @Encrypt
    public RespBean<?> getHomePageIpo(@RequestBody BaseRequest<HomePageIpoReqBody> baseRequest) {
        return this.ipoService.getHomePageIpo(baseRequest);
    }
}
