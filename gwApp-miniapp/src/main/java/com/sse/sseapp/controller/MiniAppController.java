package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.base.RespEnum;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.domain.RespContentVO;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDictData;
import com.sse.sseapp.form.request.MiniAppReqBody;
import com.sse.sseapp.service.MiniAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wy
 * @date 2023-11-29
 */
@RestController
@Slf4j
public class MiniAppController {

    @Autowired
    private MiniAppService miniAppService;

    /**
     * 获取上交所小程序服务状态
     */
    @PostMapping("/status")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("获取上交所小程序服务状态")
    public RespBean<?> getStatus() {
        return RespBean.success(miniAppService.getStatus());
    }

    /**
     * 小程序id转换上交所本地url
     */
    @PostMapping("/getUrl")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("小程序id转换上交所本地url")
    public RespBean<?> getUrl(@RequestBody MiniAppReqBody reqBody) {
        return RespBean.success(miniAppService.getUrl(reqBody));
    }

}
