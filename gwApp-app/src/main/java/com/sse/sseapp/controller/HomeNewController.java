package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.HomeNewService;
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
 * @date 2023-03-28
 */
@RestController
@Slf4j
@RequestMapping("/homeNew")
public class HomeNewController extends BaseController {

    @Autowired
    private HomeNewService homeNewService;

    /**
     * 今日新股接口
     */
    @PostMapping("/getNewSharesDataList")
    @Log("今日新股接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getNewSharesDataList(@RequestBody BaseRequest<RecommendDataReqBody> body) {
        return this.homeNewService.getNewSharesDataList(body);
    }

    /**
     * 首页推荐接口
     */
    @PostMapping("/getRecommendDataList")
    @Log("首页推荐接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getRecommendDataList(@RequestBody BaseRequest<RecommendDataReqBody> body) {
        return this.homeNewService.getRecommendDataList(body);
    }

    /**
     * 首页推荐接口CMS
     */
    @PostMapping("/getRecommendDataListCMS")
    @Log("首页推荐接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getRecommendDataListCMS(@RequestBody BaseRequest<RecommendDataReqBody> body) {
        return this.homeNewService.getRecommendDataListCMS(body);
    }

    /**
     * 首页研报接口
     */
    @PostMapping("/getResearchReportList")
    @Log("首页研报接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getResearchReportList(@RequestBody BaseRequest<RecommendDataReqBody> body) {
        return this.homeNewService.getResearchReportList(body);
    }

    /**
     * 首页研报接口
     */
    @PostMapping("/getResearchReportListCMS")
    @Log("首页研报接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getResearchReportListCMS(@RequestBody BaseRequest<RecommendDataReqBody> body) {
        return this.homeNewService.getResearchReportListCMS(body);
    }

    /**
     * 首页上交所之声接口
     */
    @PostMapping("/getSseVoicesDataList")
    @Log("首页上交所之声接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getSseVoicesDataList(@RequestBody BaseRequest<SseVoicesReqBody> body) {
        return this.homeNewService.getSseVoicesDataList(body);
    }

    /**
     * 首页上交所之声接口
     */
    @PostMapping("/getSseVoicesDataListCMS")
    @Log("首页上交所之声接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getSseVoicesDataListCMS(@RequestBody BaseRequest<SseVoicesReqBody> body) {
        return this.homeNewService.getSseVoicesDataListCMS(body);
    }

    /**
     * 首页指数接口
     */
    @PostMapping("/getIndexDataList")
    @Log("首页指数接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getIndexDataList(){
       return RespBean.success(this.homeNewService.getIndexDataList());
    }

    /**
     * 首页7X24实时资讯接口
     */
    @PostMapping("/getRealtimeInforData")
    @Log("首页7X24实时资讯接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getRealtimeInforData(@RequestBody BaseRequest<SseVoicesReqBody> body) {
        return this.homeNewService.getRealtimeInforData(body);
    }

    /**
     * 信息披露IPO规则公告接口
     */
    @PostMapping("/getIpoRuleData")
    @Log("信息披露IPO规则公告接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getIpoRuleData(@RequestBody BaseRequest<IpoRuleDataReqBody> body) {
        return this.homeNewService.getIpoRuleData(body);
    }

    /**
     * IPO询价信息接口
     */
    @PostMapping("/getIpoInquiryData")
    @Log("IPO询价信息接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getIpoInquiryData(@RequestBody BaseRequest<IpoInquiryDataReqBody> body) {
        return this.homeNewService.getIpoInquiryData(body);
    }

    /**
     * soa-app改版信息披露公司公告接口
     */
    @PostMapping("/getCompNoticeList")
    @Log("soa-app改版信息披露公司公告接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getCompNoticeList(@RequestBody BaseRequest<GetCompNoticeListReqBody> body) {
        return RespBean.success(homeNewService.getCompNoticeList(body));
    }


}
