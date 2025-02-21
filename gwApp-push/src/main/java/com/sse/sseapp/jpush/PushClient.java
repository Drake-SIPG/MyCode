package com.sse.sseapp.jpush;

import cn.jiguang.common.ClientConfig;
import com.sse.sseapp.jpush.push.PushResult;
import com.sse.sseapp.jpush.push.model.Platform;
import com.sse.sseapp.jpush.push.model.PushPayload;
import com.sse.sseapp.jpush.push.model.audience.Audience;
import com.sse.sseapp.jpush.push.model.notification.Notification;
import com.sse.sseapp.jpush.report.ReceivedsResult;
import lombok.extern.slf4j.Slf4j;

import static com.sse.sseapp.jpush.push.model.notification.PlatformNotification.ALERT;

/**
 * 极光推送
 *
 * @author zhengyaosheng
 * @date 2023-03-08
 */
@Slf4j
public class PushClient {


    public static void getReport(String appKey, String masterSecret, String msgId) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        try {
            ReceivedsResult result = jpushClient.getReportReceiveds(msgId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PushPayload buildPushObject_all_alias_alert() {
        return PushPayload.alertAll(ALERT);
    }


    public static PushPayload buildPushObject_all_alias_alert(String alias, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(content))
                .build();
    }
}
