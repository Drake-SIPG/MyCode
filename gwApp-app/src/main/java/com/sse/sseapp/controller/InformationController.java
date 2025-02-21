package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.GetAnnoucementListReqBody;
import com.sse.sseapp.service.InformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/14 11:00
 */
@RestController
@RequestMapping("/information")
@Slf4j
public class InformationController extends BaseController {

    @Autowired
    InformationService informationService;

    /**
     *  新闻发布会、上交所公告、热点动态接口
     */
    @PostMapping("/getAnnoucementList")
    @ResponseBody
    @Log("新闻发布会、上交所公告、热点动态接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getAnnoucementList(@RequestBody BaseRequest<GetAnnoucementListReqBody> getAnnoucementListReqBody){
        return RespBean.success(informationService.getAnnoucementList(getAnnoucementListReqBody));
    }

    /**
     *  新闻发布会、上交所公告、热点动态CMS接口
     */
    @PostMapping("/getAnnoucementListCMS")
    @ResponseBody
    @Log("新闻发布会、上交所公告、热点动态CMS接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getAnnoucementListCMS(@RequestBody BaseRequest<GetAnnoucementListReqBody> getAnnoucementListReqBody){
        return RespBean.success(informationService.getAnnoucementListCMS(getAnnoucementListReqBody));
    }
}
