package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.MonthReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 上证月报
 *
 * @author: liuxinyu
 * @create-date: 2023/5/24 14:44
 */
@RestController
@RequestMapping("/monthReport")
@Slf4j
public class MonthReportController extends BaseController {
    @Autowired
    MonthReportService monthReportService;

    /**
     * 上证月报转发接口
     */
    @PostMapping("/getDataList")
    @ResponseBody
    @Log("上证月报转发接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getDataList(@RequestBody BaseRequest<GetDataListReqBody> getDataList) {
        return RespBean.success(monthReportService.getDataList(getDataList));
    }

    /**
     * 获取上证统计月报--上市公司上市公司地区分布接口
     */
    @PostMapping("/getListedCompanyArea")
    @ResponseBody
    @Log("获取上证统计月报--上市公司上市公司地区分布接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getListedCompanyArea(@RequestBody BaseRequest<GetListedCompanyAreaReqBody> getListedCompanyArea) {
        return RespBean.success(monthReportService.getListedCompanyArea(getListedCompanyArea));
    }

    /**
     * 获取上证统计月报--上市公司分类情况接口
     */
    @PostMapping("/getListedCompanyOverView")
    @ResponseBody
    @Log("获取上证统计月报--上市公司分类情况接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getListedCompanyOverView(@RequestBody BaseRequest<GetListedCompanyOverViewReqBody> getListedCompanyArea) {
        return RespBean.success(monthReportService.getListedCompanyOverView(getListedCompanyArea));
    }

    /**
     * 获取上证统计月报--会员 在席位数接口
     */
    @PostMapping("/getMemberArea")
    @ResponseBody
    @Log("获取上证统计月报--会员 在席位数接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getMemberArea(@RequestBody BaseRequest<GetMemberAreaReqBody> getMemberAreaReq) {
        return RespBean.success(monthReportService.getMemberArea(getMemberAreaReq));
    }

    /**
     * 获取上证统计月报--会员 会员交易接口
     */
    @PostMapping("/getMemberTrade")
    @ResponseBody
    @Log("获取上证统计月报--会员 会员交易接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getMemberTrade(@RequestBody BaseRequest<GetMemberTradeReqBody> getMemberTrade) {
        return RespBean.success(monthReportService.getMemberTrade(getMemberTrade));
    }

    /**
     * 获取上证统计月报--会员 营业部交易接口
     */
    @PostMapping("/getBranchTrade")
    @ResponseBody
    @Log("获取上证统计月报--会员 营业部交易接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getBranchTrade(@RequestBody BaseRequest<GetBranchTradeReqBody> getBranchTrade) {
        return RespBean.success(monthReportService.getBranchTrade(getBranchTrade));
    }

    /**
     * 获取上证统计月报--会员 融资融券接口
     */
    @PostMapping("/getRzrqTrade")
    @ResponseBody
    @Log("获取上证统计月报--会员 融资融券接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getRzrqTrade(@RequestBody BaseRequest<GetRzrqTradeReqBody> getRzrqTrade) {
        return RespBean.success(monthReportService.getRzrqTrade(getRzrqTrade));
    }

}
