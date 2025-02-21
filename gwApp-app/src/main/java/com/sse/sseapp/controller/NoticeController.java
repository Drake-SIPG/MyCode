package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 接口
 *
 * @author zhengyaosheng
 * @date 2023-05-31
 */
@RestController
@Slf4j
@RequestMapping("/notice")
public class NoticeController extends BaseController {


    @Autowired
    private NoticeService noticeService;

    /**
     * 增加公告阅读数
     */
    @PostMapping("/setBulletinReadCount")
    @Log("增加公告阅读数")
    @Decrypt
    @Encrypt
    public RespBean<?> setBulletinReadCount(@RequestBody BaseRequest<BulletinReadCountReqBody> baseRequest) {
        return this.noticeService.setBulletinReadCount(baseRequest);
    }

    /**
     * 上证基金/债券公告转发接口
     */
    @PostMapping("/baseNoticeList")
    @Log("上证基金/债券公告转发接口")
    @Decrypt
    @Encrypt
    public RespBean<?> baseNoticeList(@RequestBody BaseRequest<BaseNoticeListReqBody> baseRequest) {
        return RespBean.success(noticeService.baseNoticeList(baseRequest));
    }

    /**
     * APP衍生品公告接口
     */
    @PostMapping("/derivativeNoticeList")
    @Log("APP衍生品公告接口")
    @Decrypt
    @Encrypt
    public RespBean<?> derivativeNoticeList(@RequestBody BaseRequest<DerivativeNoticeListReqBody> baseRequest) {
        return RespBean.success(noticeService.derivativeNoticeList(baseRequest));
    }

    /**
     * APP衍生品公告CMS接口
     */
    @PostMapping("/derivativeNoticeListCMS")
    @Log("APP衍生品公告CMS接口")
    @Decrypt
    @Encrypt
    public RespBean<?> derivativeNoticeListCMS(@RequestBody BaseRequest<DerivativeNoticeListReqBody> baseRequest) {
        return RespBean.success(noticeService.derivativeNoticeListCMS(baseRequest));
    }

    /**
     * supervisionList 信息获取
     */
    @PostMapping("/supervisionList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("监管公告获取")
    public RespBean supervisionList(@RequestBody BaseRequest<SupervisionListReqBody> baseRequest) {
        return this.noticeService.supervisionList(baseRequest);
    }

    /**
     * supervisionListCMS 信息获取
     */
    @PostMapping("/supervisionListCMS")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("监管公告获取CMS")
    public RespBean supervisionListCMS(@RequestBody BaseRequest<SupervisionListReqBody> baseRequest) {
        return this.noticeService.supervisionListCMS(baseRequest);
    }

    /**
     * 合约与交易公告、参与人公告接口
     */
    @PostMapping("/pactNoticeList")
    @Log("合约与交易公告、参与人公告接口")
    @Decrypt
    @Encrypt
    public RespBean<?> pactNoticeList(@RequestBody BaseRequest<PactNoticeListReqBody> baseRequest) {
        return RespBean.success(noticeService.pactNoticeList(baseRequest));
    }

    /**
     * 合约与交易公告、参与人公告接口
     */
    @PostMapping("/pactNoticeListCMS")
    @Log("合约与交易公告、参与人公告接口CMS")
    @Decrypt
    @Encrypt
    public RespBean<?> pactNoticeListCMS(@RequestBody BaseRequest<PactNoticeListReqBody> baseRequest) {
        return RespBean.success(noticeService.pactNoticeListCMS(baseRequest));
    }

}
