package com.sse.sseapp.jpush.service;


import com.sse.sseapp.jpush.PushBean;
import com.sse.sseapp.jpush.push.PushResult;
import com.sse.sseapp.jpush.report.ReceivedsResult;

/**
 *
 * @ClassName: JiGuangPushService
 * @Description: 推送服务  封装业务功能相关
 * @Author: zhengyaosheng
 * @CreateDate: 2023-03-10
 */
public interface JGPushService {
    PushResult pushAll(PushBean pushBean);

    PushResult aliasPushAll(PushBean pushBean, String... alias);

    PushResult buildCustomPushPayload(PushBean pushBean, String... alias);

    String[] checkRegistids(String[] registids);

    ReceivedsResult getReportReceived(String msgIds);

    PushResult tagPushAll(PushBean pushBean, String... tag);

}
