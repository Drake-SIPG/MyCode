package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.GetListingDataListReqBody;
import com.sse.sseapp.service.NewRegistrationSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 9:20
 */
@RestController
@RequestMapping("/registrationSystem")
@Slf4j
public class NewRegistrationSystemController extends BaseController {

    @Autowired
    private NewRegistrationSystemService registrationSystemService;

    /**
     * 注册制相关数据转发接口
     */
    @PostMapping("/getListingDataList")
    @ResponseBody
    @Log("注册制相关数据转发接口")
    @Decrypt
    @Encrypt
    public RespBean getListingDataList(@RequestBody BaseRequest<GetListingDataListReqBody> getListingDataList) {
        return RespBean.success(registrationSystemService.getListingDataList(getListingDataList));
    }


}
