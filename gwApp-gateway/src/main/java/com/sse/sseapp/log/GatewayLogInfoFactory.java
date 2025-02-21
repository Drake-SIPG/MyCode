package com.sse.sseapp.log;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.sse.sseapp.domain.system.SysGatewayLog;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangfeng
 * @date 2023/1/4 17:10
 **/
@Slf4j
public class GatewayLogInfoFactory {
    public static void log(String type, SysGatewayLog gatewayLog){
        switch (type){
            case GatewayLogType.APPLICATION_JSON_REQUEST:
            case GatewayLogType.FORM_DATA_REQUEST:
            case GatewayLogType.BASIC_REQUEST:
                log.info("[{}] {} {},route: {},status: {},excute: {} mills,requestBody: {},responseBody: {}"
                        ,gatewayLog.getIp()
                        ,gatewayLog.getRequestMethod()
                        ,gatewayLog.getRequestPath()
                        ,gatewayLog.getTargetServer()
                        ,gatewayLog.getResponseCode()
                        ,gatewayLog.getExecuteTime()
                        ,StrUtil.replace(gatewayLog.getRequestBody(), StrPool.LF,"")
                );
                break;
            case GatewayLogType.NORMAL_REQUEST:
                log.info("[{}] {} {},route: {},status: {},excute: {} mills,queryParams: {},responseBody: {}"
                        ,gatewayLog.getIp()
                        ,gatewayLog.getRequestMethod()
                        ,gatewayLog.getRequestPath()
                        ,gatewayLog.getTargetServer()
                        ,gatewayLog.getResponseCode()
                        ,gatewayLog.getExecuteTime()
                        ,gatewayLog.getQueryParams()
                );
                break;
            default:
                break;
        }
    }
}
