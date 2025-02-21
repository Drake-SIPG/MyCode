package com.sse.sseapp.controller;

import com.sse.sseapp.core.constant.ResponseBean;
import com.sse.sseapp.domain.system.SysGatewayLog;
import com.sse.sseapp.service.SysGatewayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/sysGatewayLog")
public class SysGatewayLogController {

    @Autowired
    private SysGatewayLogService sysGatewayLogService;

    /**
     * 查询路由日志集合
     */
    @PostMapping("/getGateWayLogList")
    public ResponseBean getGateWayLogList(@RequestBody SysGatewayLog sysGatewayLog){
        return this.sysGatewayLogService.getGateWayLogList(sysGatewayLog);
    }
}
