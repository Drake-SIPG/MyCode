package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.TurnoverReqBody;
import com.sse.sseapp.service.OverviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 债券成交概览
 *
 * @author wy
 * @date 2023-08-10
 */
@RestController
@RequestMapping("/overview")
@Slf4j
public class OverviewController {

    @Autowired
    private OverviewService overviewService;

    /**
     * 债券成交概览
     */
    @PostMapping("/turnover")
    @Log("债券成交概览")
    @Decrypt
    @Encrypt
    public RespBean<?> turnover(@RequestBody BaseRequest<TurnoverReqBody> body) {
        return this.overviewService.turnover(body);
    }


}
