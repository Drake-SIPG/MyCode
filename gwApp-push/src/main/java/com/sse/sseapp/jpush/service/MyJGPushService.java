package com.sse.sseapp.jpush.service;


import com.sse.sseapp.jpush.PushBean;
import com.sse.sseapp.jpush.push.PushResult;
import com.sse.sseapp.jpush.push.model.PushPayload;
import com.sse.sseapp.jpush.report.ReceivedsResult;

/**
 * @ClassName: MyJiGuangPushService
 * @Description: java类作用描述
 * @Author: zhengyaosheng
 * @CreateDate: 2023-03-10
 */
public interface MyJGPushService {
    PushResult pushAll(PushBean pushBean);

    PushResult sendPush(PushPayload pushPayload);

    PushResult aliasPushAll(PushBean pushBean, String... alias);

    PushResult buildCustomPushPayload(PushBean pushBean, String... alias);

    ReceivedsResult getReportReceived(String msgIds);

    PushResult tagPushAll(PushBean pushBean, String... tag);
}
