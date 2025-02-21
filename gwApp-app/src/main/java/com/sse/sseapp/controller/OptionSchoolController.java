package com.sse.sseapp.controller;

import cn.hutool.json.JSONObject;
import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;

import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.GetDueDateReqBody;
import com.sse.sseapp.form.request.GetSchoolsRateReqBody;
import com.sse.sseapp.form.request.OptionSchoolReqBody;
import com.sse.sseapp.form.request.SetSchoolsRateReqBody;
import com.sse.sseapp.service.OptionSchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




/**
 * @author: liuxinyu
 * @create-date: 2023/4/21 10:09
 */
@RestController
@Slf4j
@RequestMapping("/optionSchool")
public class OptionSchoolController extends BaseController {

    @Autowired
    OptionSchoolService qqxyService;

    /**
     * 期权学院通用入口
     */
    @PostMapping("/common")
    @ResponseBody
    @Log("期权学院通用入口")
    @Decrypt
    @Encrypt
    public RespBean<?> common(@RequestBody BaseRequest<OptionSchoolReqBody> reqMap) {
        return RespBean.success(qqxyService.common(reqMap));
    }

    /**
     * 保存系列课程进度
     */
    @PostMapping("/setSchoolsRate")
    @Log("保存系列课程进度")
    @Decrypt
    @Encrypt
    public RespBean<?> setSchoolsRate(@RequestBody BaseRequest<SetSchoolsRateReqBody> body) {
        return RespBean.success(this.qqxyService.setSchoolsRate(body));
    }

    /**
     * 获取系列课程进度
     */
    @PostMapping("/getSchoolsRate")
    @Log("获取系列课程进度")
    @Decrypt
    @Encrypt
    public RespBean<?> getSchoolsRate(@RequestBody BaseRequest<GetSchoolsRateReqBody> body) {
        return RespBean.success(this.qqxyService.getSchoolsRate(body));
    }



    /**
     * 获取到期日
     */
    @PostMapping("/optionCalculator/getDueDate")
    @Log("获取到期日")
    @Decrypt
    @Encrypt
    public JSONObject getDueDate(@RequestBody BaseRequest<GetDueDateReqBody> body) {
        return this.qqxyService.getDueDate(body);
    }



}
