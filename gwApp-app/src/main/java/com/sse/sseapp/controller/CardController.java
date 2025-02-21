package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.AddShareHolderCardReqBody;
import com.sse.sseapp.form.request.GetActiviteCode4AppReqBody;
import com.sse.sseapp.form.request.GetShareholderCard4AppReqBody;
import com.sse.sseapp.form.request.GetSpecialShareholderCardStatusReqBody;
import com.sse.sseapp.form.response.AddShareHolderCardResBody;
import com.sse.sseapp.form.response.GetActiviteCode4AppResBody;
import com.sse.sseapp.form.response.GetShareholderCard4AppResBody;
import com.sse.sseapp.form.response.GetSpecialShareholderCardStatusResBody;
import com.sse.sseapp.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 股东卡号绑定
 */
@RestController
@RequestMapping("/card")
@Slf4j
public class CardController extends BaseController {
    @Autowired
    CardService cardService;

    /**
     * 绑定股东卡号-绑定卡号
     */
    @PostMapping("/addShareHolderCard")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("绑定股东卡号-绑定卡号")
    public RespBean<AddShareHolderCardResBody> addShareHolderCard(@RequestBody BaseRequest<AddShareHolderCardReqBody> reqBody) {
        return success(cardService.addShareHolderCard(reqBody));
    }

    /**
     * 绑定股东卡号-获取激活码
     */
    @PostMapping("/getActiviteCode4App")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("绑定股东卡号-获取激活码")
    public RespBean<GetActiviteCode4AppResBody> getActiviteCode4App(@RequestBody BaseRequest<GetActiviteCode4AppReqBody> reqBody) {
        return success(cardService.getActiviteCode4App(reqBody));
    }

    /**
     * 绑定股东卡号-查询股东卡号列表
     */
    @PostMapping("/getShareholderCard4App")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("绑定股东卡号-查询股东卡号列表")
    public RespBean<GetShareholderCard4AppResBody> getShareholderCard4App(@RequestBody BaseRequest<GetShareholderCard4AppReqBody> reqBody) {
        return success(cardService.getShareholderCard4App(reqBody));
    }

    /**
     * 绑定股东卡号-查询激活状态
     */
    @PostMapping("/getSpecialShareholderCardStatus")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("绑定股东卡号-查询激活状态")
    public RespBean<GetSpecialShareholderCardStatusResBody> getSpecialShareholderCardStatus(@RequestBody BaseRequest<GetSpecialShareholderCardStatusReqBody> reqBody) {
        return success(cardService.getSpecialShareholderCardStatus(reqBody));
    }
}
