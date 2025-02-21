package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.MarginTradingNoticeReqBody;
import com.sse.sseapp.form.request.MarginTradingReqBody;
import com.sse.sseapp.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subject")
@Slf4j
public class SubjectController extends BaseController {

    @Autowired
    private SubjectService subjectService;


    /**
     * 股票期权 页面整体 接口
     */
    @PostMapping("/marginTrading")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("股票期权 页面整体 接口")
    public RespBean<?> marginTrading(@RequestBody BaseRequest<MarginTradingReqBody> reqBody) {
        return this.subjectService.marginTrading(reqBody);
    }

    /**
     * 股票期权 页面整体 接口
     */
    @PostMapping("/marginTradingCMS")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("股票期权 页面整体 接口CMS")
    public RespBean<?> marginTradingCMS(@RequestBody BaseRequest<MarginTradingReqBody> reqBody) {
        return this.subjectService.marginTradingCMS(reqBody);
    }
    /**
     * 融资融券公告
     */
    @PostMapping("/marginTradingNotice")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("融资融券公告")
    public RespBean<?> marginTradingNotice(@RequestBody BaseRequest<MarginTradingNoticeReqBody> reqBody) {
        return RespBean.success(subjectService.marginTradingNotice(reqBody));
    }

    /**
     * 融资融券公告CMS
     */
    @PostMapping("/marginTradingNoticeCMS")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("融资融券公告CMS")
    public RespBean<?> marginTradingNoticeCMS(@RequestBody BaseRequest<MarginTradingNoticeReqBody> reqBody) {
        return RespBean.success(subjectService.marginTradingNoticeCMS(reqBody));
    }

}
