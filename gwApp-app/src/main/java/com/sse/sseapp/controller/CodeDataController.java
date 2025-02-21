package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.CodeDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/codeData")
public class CodeDataController extends BaseController {
    @Autowired
    private CodeDataService service;

    /**
     * 股票数据总貌统计数据
     */
    @PostMapping("/dataOverview")
    @Log("股票数据总貌统计数据")
    @Decrypt
    @Encrypt
    public RespBean<?> dataOverview(@RequestBody BaseRequest<DataOverviewReqBody> body) {
        return RespBean.success(service.dataOverview(body).getResult());
    }

    /**
     * 上市公司披露报告列表接口
     */
    @PostMapping("/disclosureReportList")
    @Log("上市公司披露报告列表接口")
    @Decrypt
    @Encrypt
    public RespBean<?> disclosureReportList(@RequestBody BaseRequest<DisclosureReportListReqBody> body) {
        return RespBean.success(service.disclosureReportList(body).getResult());
    }


    /**
     * 每日基金情况、每日成交概况
     */
    @PostMapping("/dailyFundOverview")
    @Log("每日基金情况、每日成交概况")
    @Decrypt
    @Encrypt
    public RespBean<?> dailyFundOverview(@RequestBody BaseRequest<DailyFundOverviewReqBody> body) {
        return RespBean.success(service.dailyFundOverview(body).getResult());
    }

    /**
     * 主板、科创板市价总值、流通市值排名前十
     */
    @PostMapping("/valueRanking")
    @Log("主板、科创板市价总值、流通市值排名前十")
    @Decrypt
    @Encrypt
    public RespBean<?> valueRanking(@RequestBody BaseRequest<ValueRankingReqBody> body) {
        return RespBean.success(service.valueRanking(body).getResult());
    }

    /**
     * 活跃股排名前十
     */
    @PostMapping("/activityRanking")
    @Log("活跃股排名前十")
    @Decrypt
    @Encrypt
    public RespBean<?> activityRanking(@RequestBody BaseRequest<ActivityRankingReqBody> body) {
        return RespBean.success(service.activityRanking(body).getResult());
    }

}
