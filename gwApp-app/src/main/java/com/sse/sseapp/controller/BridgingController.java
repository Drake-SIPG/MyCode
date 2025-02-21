package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.ChannelCategoryDataReqBody;
import com.sse.sseapp.service.BridgingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/31 10:13
 */
@RestController
@RequestMapping("/bridging")
@Slf4j
public class BridgingController extends BaseController {
    @Autowired
    private BridgingService bridgingService;

    /**
     * 桥接获取 hot key
     */
    @PostMapping("/getChannelCategoryData")
    @Log("桥接获取 hot key")
    @Decrypt
    @Encrypt
    public RespBean<?> getChannelCategoryData(@RequestBody BaseRequest<ChannelCategoryDataReqBody> body) {
        return RespBean.success(bridgingService.getChannelCategoryData(body));
    }



}
