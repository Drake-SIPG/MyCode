package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.BondMarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 债券做市
 *
 * @author wy
 * @date 2023-08-07
 */
@RestController
@RequestMapping("/bondMarket")
@Slf4j
public class BondMarketController extends BaseController {

    @Autowired
    private BondMarketService bondMarketService;

    /**
     * 做市场
     */
    @PostMapping("/marketMaker")
    @Log("做市场")
    @Decrypt
    @Encrypt
    public RespBean<?> marketMaker(@RequestBody BaseRequest<MarketMakerReqBody> body) {
        return this.bondMarketService.marketMaker(body);
    }

    /**
     * 做市品种调整信息
     */
    @PostMapping("/marketMakingInfo")
    @Log("做市品种调整信息")
    @Decrypt
    @Encrypt
    public RespBean<?> marketMakingInfo(@RequestBody BaseRequest<MarketMakingInfoReqBody> body) {
        return this.bondMarketService.marketMakingInfo(body);
    }

    /**
     * 做事品种列表
     */
    @PostMapping("/marketMakingList")
    @Log("做事品种列表")
    @Decrypt
    @Encrypt
    public RespBean<?> marketMakingList(@RequestBody BaseRequest<MarketMakingListReqBody> body) {
        return this.bondMarketService.marketMakingList(body);
    }

    /**
     * 自选做市品种
     */
    @PostMapping("/marketMakingSelf")
    @Log("自选做市品种")
    @Decrypt
    @Encrypt
    public RespBean<?> marketMakingSelf(@RequestBody BaseRequest<MarketMakingSelfReqBody> body) {
        return this.bondMarketService.marketMakingSelf(body);
    }

    /**
     * 基准做市业务情况
     */
    @PostMapping("/marketMakingBusinessInfo")
    @Log("基准做市业务情况")
    @Decrypt
    @Encrypt
    public RespBean<?> marketMakingBusinessInfo(@RequestBody BaseRequest<MarketMakingBusinessInfoReqBody> body) {
        return this.bondMarketService.marketMakingBusinessInfo(body);
    }

    /**
     * 季度优秀做市商
     */
    @PostMapping("/marketMakingSeason")
    @Log("季度优秀做市商")
    @Decrypt
    @Encrypt
    public RespBean<?> marketMakingSeason(@RequestBody BaseRequest<MarketMakingSeasonReqBody> body) {
        return this.bondMarketService.marketMakingSeason(body);
    }

    /**
     * 年度优秀做市商
     */
    @PostMapping("/marketMakingYear")
    @Log("年度优秀做市商")
    @Decrypt
    @Encrypt
    public RespBean<?> marketMakingYear(@RequestBody BaseRequest<MarketMakingYearReqBody> body) {
        return this.bondMarketService.marketMakingYear(body);
    }

    /**
     * 债券公告
     */
    @PostMapping("/marketNotice")
    @Log("债券公告")
    @Decrypt
    @Encrypt
    public RespBean<?> marketNotice(@RequestBody BaseRequest<MarketNoticeReqBody> body) {
        return this.bondMarketService.marketNotice(body);
    }

    /**
     * 新债券交易系统综合排名
     */
    @PostMapping("/bondRanking")
    @Log("新债券交易系统综合排名")
    @Decrypt
    @Encrypt
    public RespBean<?> bondRanking(@RequestBody BaseRequest<BondRankingReqBody> body) {
        return this.bondMarketService.bondRanking(body);
    }

}
