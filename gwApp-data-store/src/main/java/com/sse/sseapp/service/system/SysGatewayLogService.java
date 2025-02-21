package com.sse.sseapp.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.system.SysGatewayLog;

/**
 * <p>
 * 路由日志表 服务类
 * </p>
 *
 * @author wangfeng
 * @since 2023-01-05
 */
public interface SysGatewayLogService extends IService<SysGatewayLog> {

    /**
     * 保存路由日志
     * @param sysGatewayLog
     * @return
     */
    Boolean saveGateWayLog(SysGatewayLog sysGatewayLog);

    /**
     * 查询路由日志集合
     * @param sysGatewayLog
     * @return
     */
    Page<SysGatewayLog> getGateWayLogList(SysGatewayLog sysGatewayLog);


}
