package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.annotation.RepeatSubmit;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * person 接口
 *
 * @author zhengyaosheng
 * @date 2023-04-03
 */
@RestController
@Slf4j
@RequestMapping("/person")
public class PersonController extends BaseController {

    @Autowired
    private PersonService personService;


    /**
     * 收藏-收藏列表
     */
    @PostMapping("/favouriteList")
    @Log("收藏列表")
    @Decrypt
    @Encrypt
    public RespBean<?> favouriteList(@RequestBody BaseRequest<FavouriteReqBody> baseRequest) {
        return this.personService.favouriteList(baseRequest);
    }

    /**
     * 收藏-是否已收藏(person/isFavourite)
     */
    @PostMapping("/isFavourite")
    @Log("是否已收藏")
    @Decrypt
    @Encrypt
    public RespBean<?> isFavourite(@RequestBody BaseRequest<FavouriteReqBody> baseRequest) {
        return this.personService.isFavourite(baseRequest);
    }

    /**
     * 收藏-添加收藏(person/addFavourite)
     */
    @PostMapping("/addFavourite")
    @Log("添加收藏")
    @Decrypt
    @Encrypt
    public RespBean<?> addFavourite(@RequestBody BaseRequest<AddFavouriteReqBody> baseRequest) {
        return this.personService.addFavourite(baseRequest);
    }

    /**
     * 收藏-取消收藏(person/removeFavourite)
     */
    @PostMapping("/removeFavourite")
    @Log("取消收藏")
    @Decrypt
    @Encrypt
    public RespBean<?> removeFavourite(@RequestBody BaseRequest<RemoveFavouriteReqBody> baseRequest) {
        return this.personService.removeFavourite(baseRequest);
    }

    /**
     * 自选-自选股列表
     */
    @PostMapping("/optionalStockList")
    @Log("自选股列表")
    @Decrypt
    @Encrypt
    public RespBean<?> optionalStockList(@RequestBody BaseRequest<OptionalStockReqBody> baseRequest) {
        return RespBean.success(this.personService.optionalStockList(baseRequest));
    }

    /**
     * 自选-添加自选
     */
    @PostMapping("/addOptionalStock")
    @Log("添加自选")
    @Decrypt
    @Encrypt
    public RespBean<?> addOptionalStock(@RequestBody BaseRequest<AddOptionalStockReqBody> baseRequest) {
        return this.personService.addOptionalStock(baseRequest);
    }

    /**
     * 自选-取消自选
     */
    @PostMapping("/removeOptionalStock")
    @Log("取消自选")
    @Decrypt
    @Encrypt
    public RespBean<?> removeOptionalStock(@RequestBody BaseRequest<RemoveOptionalStockReqBody> baseRequest) {
        return this.personService.removeOptionalStock(baseRequest);
    }

    /**
     * 自选-股票搜索
     */
    @PostMapping("/marketMainSharesList")
    @Log("股票搜索")
    @Decrypt
    @Encrypt
    public RespBean<?> marketMainSharesList(@RequestBody BaseRequest<MarketMainSharesReqBody> baseRequest) {
        return RespBean.success(this.personService.marketMainSharesList(baseRequest));
    }

    /**
     * 公告-公告列表 noticeList
     */
    @PostMapping("/noticeList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("公告列表")
    public RespBean noticeList(@RequestBody BaseRequest<NoticeReqBody> baseRequest) {
        return RespBean.success(this.personService.noticeList(baseRequest));
    }

    /**
     * 公告-公告列表 noticeList
     */
    @PostMapping("/noticeListCMS")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("公告列表CMS")
    public RespBean noticeListCMS(@RequestBody BaseRequest<NoticeReqBody> baseRequest) {
        return RespBean.success(this.personService.noticeListCMS(baseRequest));
    }

    /**
     * 自选-是否已自选
     */
    @PostMapping("/isOptionalStock")
    @Log("是否已自选")
    @Decrypt
    @Encrypt
    public RespBean<?> isOptionalStock(@RequestBody BaseRequest<IsOptionalStockReqBody> baseRequest) {
        return this.personService.isOptionalStock(baseRequest);
    }

    /**
     * 添加我的反馈
     */
    @PostMapping("/addFeedback")
    @Log("添加我的反馈")
    @Decrypt
    @Encrypt
    @ResponseBody
    public RespBean<?> addFeedback(@RequestBody BaseRequest<AddFeedbackReqBody> baseRequest) {
        return this.personService.addFeedback(baseRequest);
    }

    /**
     * 获取图形验证码
     */
    @PostMapping("/getImageVerificationCode")
    @Log("获取图形验证码")
    @Decrypt
    @Encrypt
    @ResponseBody
    @RepeatSubmit(interval = 2000)
    public RespBean<?> getImageVerificationCode(@RequestBody BaseRequest<CommonReqBody> baseRequest) {
        return this.personService.getImageVerificationCode(baseRequest);
    }

    /**
     * supervisionList 信息获取
     */
    @PostMapping("/supervisionList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("supervisionList公告获取")
    public RespBean supervisionList(@RequestBody BaseRequest<SupervisionListReqBody> baseRequest) {
        return this.personService.supervisionList(baseRequest);
    }

    /**
     * 党建 1:学习园地  2:党建动态 3:图文播报
     */
    @PostMapping("/partyList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("党建")
    public RespBean partyList(@RequestBody BaseRequest<PartyListReqBody> baseRequest) {
        return this.personService.partyList(baseRequest);
    }

    /**
     * 获取我的页面 我的消息总数 我的自选总数 我的收藏总数
     */
    @PostMapping("/getPersonDataCount")
    @ResponseBody
    @Log("获取我的页面 我的消息总数 我的自选总数 我的收藏总数")
    @Decrypt
    @Encrypt
    public RespBean getPersonDataCount(@RequestBody BaseRequest<GetPersonDataCountReqBody> baseRequest) {
        return success(personService.getPersonDataCount(baseRequest));
    }

}
