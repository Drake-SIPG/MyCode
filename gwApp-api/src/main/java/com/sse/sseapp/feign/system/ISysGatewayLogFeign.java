package com.sse.sseapp.feign.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.domain.system.SysGatewayLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 路由管理日志feign
 * @author wangfeng
 * @date 2023/1/5 16:30
 **/
@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/sysGatewayLog")
public interface ISysGatewayLogFeign {

    /**
     * 保存路由管理日志
     * @param sysGatewayLog
     */
    @PostMapping("/saveGateWayLog")
    void saveGateWayLog(@RequestBody SysGatewayLog sysGatewayLog);

    /**
     * 查询路由日志集合
     * @param sysGatewayLog
     * @return
     */
    @PostMapping("/getGateWayLogList")
    Page<SysGatewayLog> getGateWayLogList(@RequestBody SysGatewayLog sysGatewayLog);
}
