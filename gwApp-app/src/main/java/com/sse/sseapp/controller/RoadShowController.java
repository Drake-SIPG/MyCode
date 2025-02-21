package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.RoadShowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/13 17:28
 */
@RestController
@RequestMapping("/roadShow")
@Slf4j
public class RoadShowController {

    @Autowired
    RoadShowService roadShowService;

    /**
     * 行情-详情页面-路演
     */
    @PostMapping("/getRoadShowListByPage")
    @Log("行情-详情页面-路演")
    @Decrypt
    @Encrypt
    public RespBean<?> getRoadShowListByPage(@RequestBody BaseRequest<GetRoadShowListByPageReqBody> getRoadShowListByPageReqBody) {
        return RespBean.success(this.roadShowService.getRoadShowListByPage(getRoadShowListByPageReqBody));
    }

    /**
     * 最新路演列表
     */
    @PostMapping("/newRoadShowList")
    @Log("最新路演列表")
    @Decrypt
    @Encrypt
    public RespBean<?> newRoadShowList(@RequestBody BaseRequest<RoadShowListReqBody> baseRequest) {
        return this.roadShowService.newRoadShowList(baseRequest);
    }

    /**
     * 回顾路演列表
     */
    @PostMapping("/backRoadShowList")
    @Log("回顾路演列表")
    @Decrypt
    @Encrypt
    public RespBean<?> backRoadShowList(@RequestBody BaseRequest<RoadShowListReqBody> baseRequest) {
        return this.roadShowService.backRoadShowList(baseRequest);
    }

    /**
     * 路演嘉宾列表
     */
    @PostMapping("/guestList")
    @Log("路演嘉宾列表")
    @Decrypt
    @Encrypt
    public RespBean<?> guestList(@RequestBody BaseRequest<GuestListReqBody> baseRequest) {
        return this.roadShowService.guestList(baseRequest);
    }

    /**
     * e访谈发送提问
     */
    @PostMapping("/talkAsk")
    @Log("e访谈发送提问")
    @Decrypt
    @Encrypt
    public RespBean<?> talkAsk(@Validated @RequestBody BaseRequest<TalkAskReqBody> baseRequest) {
        return this.roadShowService.talkAsk(baseRequest);
    }

    /**
     * e访谈发送提问
     */
    @PostMapping("/getRoadShowButton")
    @Log("路演按钮是否展示")
    @Decrypt
    @Encrypt
    public RespBean<?> getRoadShowButton(@RequestBody BaseRequest<GetRoadShowButtonReqBody> baseRequest) {
        return RespBean.success(roadShowService.getRoadShowButton(baseRequest));
    }
}
