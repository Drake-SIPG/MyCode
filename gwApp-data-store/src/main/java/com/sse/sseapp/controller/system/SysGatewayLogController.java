package com.sse.sseapp.controller.system;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.domain.system.SysGatewayLog;
import com.sse.sseapp.service.system.SysGatewayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 路由日志表 前端控制器
 * </p>
 *
 * @author wangfeng
 * @since 2023-01-05
 */
@RestController
@RequestMapping("/dataStore/system/sysGatewayLog")
public class SysGatewayLogController {

    /**
     * 路由日志表 服务类
     */
    @Autowired
    private SysGatewayLogService sysGatewayLogService;

    /**
     * 保存路由管理日志
     * @param sysGatewayLog
     */
    @PostMapping("/saveGateWayLog")
    public void saveGateWayLog(@RequestBody SysGatewayLog sysGatewayLog){
        this.sysGatewayLogService.saveGateWayLog(sysGatewayLog);
    }

    /**
     * 查询路由日志集合
     * @param sysGatewayLog
     * @return
     */
    @PostMapping("/getGateWayLogList")
    public Page<SysGatewayLog> getGateWayLogList(@RequestBody SysGatewayLog sysGatewayLog){
        return this.sysGatewayLogService.getGateWayLogList(sysGatewayLog);
    }

}
