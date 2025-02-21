package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@Slf4j
public class RegisterController extends BaseController {

    @Autowired
    private RegisterService registerService;


    /**
     * 科创版数据统计
     */
    @PostMapping("/getProjectStatisticInfo")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("科创版数据统计")
    public RespBean<?> getProjectStatisticInfo(@RequestBody BaseRequest<ProjectStatisticInfoReqBody> reqBody) {
        return this.registerService.getProjectStatisticInfo(reqBody);
    }

    /**
     * 科创版数据统计 按行业统计
     */
    @PostMapping("/getMoreCsrcStatistic")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("科创版数据统计 按行业统计")
    public RespBean<?> getMoreCsrcStatistic(@RequestBody BaseRequest<ProjectStatisticInfoReqBody> reqBody) {
        return this.registerService.getMoreCsrcStatistic(reqBody);
    }

    /**
     * 科创版数据统计 按地区统计
     */
    @PostMapping("/getMoreProvinceStatistic")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("科创版数据统计 按地区统计")
    public RespBean<?> getMoreProvinceStatistic(@RequestBody BaseRequest<ProjectStatisticInfoReqBody> reqBody) {
        return this.registerService.getMoreProvinceStatistic(reqBody);
    }

    /**
     * 查询科创板中介机构数据统计信息
     */
    @PostMapping("/getMoreIntermediaryStatistic")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("查询科创板中介机构数据统计信息")
    public RespBean<?> getMoreIntermediaryStatistic(@RequestBody BaseRequest<IntermediaryStatisticReqBody> reqBody) {
        return this.registerService.getMoreIntermediaryStatistic(reqBody);
    }

    /**
     * 项目列表  项目和机构是N-N
     */
    @PostMapping("/getRegisterProjectList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("项目列表  项目和机构是N-N")
    public RespBean<?> getRegisterProjectList(@RequestBody BaseRequest<RegisterProjectListReqBody> reqBody) {
        return this.registerService.getRegisterProjectList(reqBody);
    }

    /**
     * 公告
     */
    @PostMapping("/getRegisterNoticeList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("公告")
    public RespBean<?> getRegisterNoticeList(@RequestBody BaseRequest<RegisterNoticeListReqBody> reqBody) {
        return this.registerService.getRegisterNoticeListNew(reqBody);
    }

    /**
     * 公告CMS
     */
    @PostMapping("/getRegisterNoticeListCMS")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("公告CMS")
    public RespBean<?> getRegisterNoticeListCMS(@RequestBody BaseRequest<RegisterNoticeListReqBody> reqBody) {
        return this.registerService.getRegisterNoticeListNewCMS(reqBody);
    }

    /**
     * 公司公告CMS
     */
    @PostMapping("/noticeListCMS")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("注册制-公司公告CMS")
    public RespBean<?> noticeListCMS(@RequestBody BaseRequest<KCBNoticeNewListReqBody> reqBody) {
        return this.registerService.getNoticeListCMS(reqBody);
    }

    /**
     * 保荐机构列表
     */
    @PostMapping("/getIntermediaryInfoList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("保荐机构列表")
    public RespBean<?> getIntermediaryInfoList(@RequestBody BaseRequest<IntermediaryInfoListReqBody> reqBody) {
        return this.registerService.getIntermediaryInfoList(reqBody);
    }

    /**
     * 科创版首页
     */
    @PostMapping("/getKCBHomeList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("科创版首页")
    public RespBean<?> getKCBHomeList(@RequestBody BaseRequest<CommonReqBody> reqBody) {
        return this.registerService.getKCBHomeList(reqBody);
    }

    /**
     * 科创版首页
     */
    @PostMapping("/getKCBHomeListCMS")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("科创版首页")
    public RespBean<?> getKCBHomeListCMS(@RequestBody BaseRequest<CommonReqBody> reqBody) {
        return this.registerService.getKCBHomeListCMS(reqBody);
    }


    /**
     * 科创版公告
     * 100:监管信息-监管动态 101:监管信息-监管措施 102:监管信息-监管问询 199:监管信息-全部
     * 200:上交所公告-上交所公告 299:上交所公告-全部
     */
    @PostMapping("/geKCBNoticeList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("科创版公告")
    public RespBean<?> geKCBNoticeList(@RequestBody BaseRequest<KCBNoticeListReqBody> reqBody) {
        return this.registerService.geKCBNoticeListNew(reqBody);
    }


    /**
     * 科创版公告
     * 100:监管信息-监管动态 101:监管信息-监管措施 102:监管信息-监管问询 199:监管信息-全部
     * 200:上交所公告-上交所公告 299:上交所公告-全部
     */
    @PostMapping("/geKCBNoticeListCMS")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("科创版公告")
    public RespBean<?> geKCBNoticeListCMS(@RequestBody BaseRequest<KCBNoticeListReqBody> reqBody) {
        return this.registerService.geKCBNoticeListNewCMS(reqBody);
    }


    /**
     * 公司公告
     */
        @PostMapping("/noticeList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("注册制-公司公告")
    public RespBean<?> noticeList(@RequestBody BaseRequest<KCBNoticeNewListReqBody> reqBody) {
        return this.registerService.getNoticeList(reqBody);
    }
}
