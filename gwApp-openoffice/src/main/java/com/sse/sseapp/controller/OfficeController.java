package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.OfficeReqBody;
import com.sse.sseapp.service.OfficeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文件转换
 *
 * @author wy
 * @date 2023-06-02
 */
@RestController
@RequestMapping("/office")
@Slf4j
public class OfficeController extends BaseController {

    @Autowired
    OfficeService officeService;

    /**
     * 文件转换pdf
     */
    @PostMapping("/convertPdf")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("文件转换pdf")
    public RespBean<?> convertPdf(@Validated @RequestBody BaseRequest<OfficeReqBody> officeReqBodyBaseRequest) {
        return RespBean.success(officeService.convertPdf(officeReqBodyBaseRequest));
    }

}
