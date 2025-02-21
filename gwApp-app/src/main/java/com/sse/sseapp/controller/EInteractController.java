package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.EInteractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/3 17:41
 */
@RestController
@RequestMapping("/eInteract")
@Slf4j
public class EInteractController extends BaseController {

    @Autowired
    EInteractService serviceCenterService;

    /**
     *  e互动 公司发布
     */
    @PostMapping("/companyPublish")
    @ResponseBody
    @Log("e互动公司发布")
    @Decrypt
    @Encrypt
    public RespBean<?> companyPublish(@RequestBody BaseRequest<CompanyPublishReqBody> companyPublishReqBody){
        return RespBean.success(serviceCenterService.companyPublish(companyPublishReqBody));
    }

    /**
     * e互动 提问列表
     */
    @PostMapping("/ehdqasQ")
    @ResponseBody
    @Log("e互动提问列表")
    @Decrypt
    @Encrypt
    public RespBean<?> ehdqasQ(@RequestBody BaseRequest<EhdqasQReqBody> ehdqasQReqBody){
        return RespBean.success(serviceCenterService.ehdqasQ(ehdqasQReqBody));
    }

    /**
     * e互动 答复列表
     */
    @PostMapping("/ehdqasQA")
    @ResponseBody
    @Log("e互动答复列表")
    @Decrypt
    @Encrypt
    public RespBean<?> ehdqasQA(@RequestBody BaseRequest<EhdqasQAReqBody> ehdqasQAReqBody){
        return RespBean.success(serviceCenterService.ehdqasQA(ehdqasQAReqBody));
    }

    /**
     * e互动 年报视频
     */
    @PostMapping("/findCompanyVideos")
    @ResponseBody
    @Log("e互动年报视频")
    @Decrypt
    @Encrypt
    public RespBean<?> findCompanyVideos(@RequestBody BaseRequest<FindCompanyVideosReqBody> findCompanyVideosReqBody){
        return RespBean.success(serviceCenterService.findCompanyVideos(findCompanyVideosReqBody));
    }

    /**
     * e互动提问 股票搜索
     */
    @PostMapping("/marketMainSharesList")
    @ResponseBody
    @Log("e互动股票搜索")
    @Decrypt
    @Encrypt
    public RespBean<?> marketMainSharesList(@RequestBody BaseRequest<MarketMainSharesListReqBody> marketMainSharesListReqBody){
        return RespBean.success(serviceCenterService.marketMainSharesList(marketMainSharesListReqBody));
    }

    /**
     * 行情-详情页面-互动
     */
    @PostMapping("/interactionList")
    @ResponseBody
    @Log("e互动行情详情互动")
    @Decrypt
    @Encrypt
    public RespBean<?> interactionList(@RequestBody BaseRequest<InteractionListReqBody> interactionListReqBody){
        return RespBean.success(serviceCenterService.interactionList(interactionListReqBody));
    }

    /**
     * 公司详情
     */
    @PostMapping("/getCompanyDetail")
    @ResponseBody
    @Log("公司详情")
    @Decrypt
    @Encrypt
    public RespBean<?> getCompanyDetail(@RequestBody BaseRequest<CompanyDetailReqBody> baseRequest){
        return RespBean.success(serviceCenterService.getCompanyDetail(baseRequest));
    }

    /**
     * 获取e访谈是否有live
     */
    @PostMapping("/getELive")
    @ResponseBody
    @Log("获取e访谈是否有live")
    @Decrypt
    @Encrypt
    public RespBean<?> getELive(@RequestBody BaseRequest<GetELiveReqBody> baseRequest){
        return RespBean.success(serviceCenterService.getELive(baseRequest));
    }

}
