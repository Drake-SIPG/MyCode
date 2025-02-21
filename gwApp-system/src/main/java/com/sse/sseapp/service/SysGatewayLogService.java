package com.sse.sseapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.core.constant.ResponseBean;
import com.sse.sseapp.domain.system.SysGatewayLog;
import com.sse.sseapp.feign.system.ISysGatewayLogFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统管理-日志管理service
 * @author zhangwj
 * @date 2023/02/06
 */
@Slf4j
@Service
public class SysGatewayLogService {
    /**
     * 路由日志类 feign
     */
    @Autowired
    private ISysGatewayLogFeign iSysGatewayLogFeign;

    /**
     * 查询路由日志集合
     */
    public ResponseBean getGateWayLogList(SysGatewayLog sysGatewayLog){
            Page<SysGatewayLog> gateWayLogList = this.iSysGatewayLogFeign.getGateWayLogList(sysGatewayLog);
            return ResponseBean.success(gateWayLogList);
    }

}
