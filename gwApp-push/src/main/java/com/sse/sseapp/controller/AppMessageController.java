package com.sse.sseapp.controller;

import com.sse.sseapp.core.constant.ResponseBean;
import com.sse.sseapp.domain.push.AppMessage;
import com.sse.sseapp.service.AppMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 队列消息 controller
 *
 * @author zhengyaosheng
 * @date 2023-03-13
 */
@RestController
@RequestMapping("/push/appMessage")
public class AppMessageController {

    @Autowired
    private AppMessageService service;

    /**
     * ：table一览数据查询
     */
    @PostMapping("/getData")
    public ResponseBean getData(@RequestBody AppMessage entity) {
        return service.getData(entity);
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/searchById")
    public ResponseBean searchById(@RequestParam("id") String id) {
        return service.searchById(id);
    }

    /**
     * 重发
     *
     * @param id
     * @return
     */
    @GetMapping("/retransmission")
    public ResponseBean retransmission(@RequestParam("id") String id) {
        return service.retransmission(id);
    }

}
