package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.AppMessageDeviceReqBody;
import com.sse.sseapp.service.AppMessageDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * app消息设备绑定
 *
 * @author wy
 */
@RestController
@RequestMapping("/appMessageDevice")
@Slf4j
public class AppMessageDeviceController extends BaseController {

    @Autowired
    private AppMessageDeviceService appMessageDeviceService;

    /**
     * app消息设备绑定
     *
     * @param reqBody
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("app消息设备绑定")
    public RespBean<?> add(@Validated @RequestBody BaseRequest<AppMessageDeviceReqBody> reqBody) {
        int row = this.appMessageDeviceService.add(reqBody);
        if (row > 0) {
            return RespBean.success();
        } else {
            return RespBean.error();
        }
    }

}
