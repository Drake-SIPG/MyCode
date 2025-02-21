package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.RuleReqBody;
import com.sse.sseapp.service.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mateng
 * @since 2023/5/31 14:35
 */
@RestController
@Slf4j
@RequestMapping("/rule")
public class RuleController extends BaseController {

    @Autowired
    private RuleService ruleService;

    /**
     * 规则-规则获取
     */
    @PostMapping("/lastList")
    @Log("最新规则获取")
    @Decrypt
    @Encrypt
    public RespBean<?> getLastRuleList(@RequestBody BaseRequest<RuleReqBody> baseRequest) {
        return this.ruleService.getLastRuleList(baseRequest);
    }
}
