package com.sse.sseapp.jpush.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.connection.HttpProxy;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.sse.sseapp.constants.QueueConstants;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.jpush.JPushClient;
import com.sse.sseapp.jpush.PushBean;
import com.sse.sseapp.jpush.push.PushResult;
import com.sse.sseapp.jpush.push.model.Message;
import com.sse.sseapp.jpush.push.model.Options;
import com.sse.sseapp.jpush.push.model.Platform;
import com.sse.sseapp.jpush.push.model.PushPayload;
import com.sse.sseapp.jpush.push.model.audience.Audience;
import com.sse.sseapp.jpush.push.model.notification.AndroidNotification;
import com.sse.sseapp.jpush.push.model.notification.IosAlert;
import com.sse.sseapp.jpush.push.model.notification.IosNotification;
import com.sse.sseapp.jpush.push.model.notification.Notification;
import com.sse.sseapp.jpush.report.ReceivedsResult;
import com.sse.sseapp.jpush.service.MyJGPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName: MyJiGuangPushServiceImpl
 * @Description: 极光推送  封装第三方api相关
 * @Author: zhengyaosheng
 */

@Service
@Slf4j
public class MyJGPushServiceImpl implements MyJGPushService {
    @Autowired
    private ISysConfigFeign sysConfigFeign;
    @Value("${PUSH.apnsProduction}")
    private String apns;

    /**
     * 广播 (所有平台，所有设备, 不支持附加信息)
     *
     * @param pushBean 推送内容
     * @return
     */
    @Override
    public PushResult pushAll(PushBean pushBean) {
        return sendPush(PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setNotification(Notification.alert(pushBean.getAlert()))
                .build());
    }

    /**
     * 所有平台根据别名推送
     *
     * @param pushBean 推送内容
     * @return 如果目标平台为 iOS 平台 需要在 options中通过 apns_production 字段来制定推送环境。True 表示推送生产环境，False 表示要推送开发环境；
     * 如果不指定则为推送生产环境
     */
    @Override
    public PushResult aliasPushAll(PushBean pushBean, String... alias) {
        String content = pushBean.getAlert();
        IosAlert alert = IosAlert.newBuilder().setTitleAndBody(pushBean.getTitle(), null, content).build();
        return sendPush(PushPayload.newBuilder().setPlatform(Platform.android_ios())
                // 设置平台
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(content).setTitle(pushBean.getTitle()).addExtras(pushBean.getExtras()).build())
                        .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).setAlert(alert).addExtra(pushBean.getTitle(), content).addExtras(pushBean.getExtras()).build()).build())
                .setOptions(Options.newBuilder().setApnsProduction(Boolean.parseBoolean(apns)).build()).build());//iOS下开发环境能收到，生产环境收不到（API 推送设置可选参数 options——环境参数 apns_production，true 为生产、false 是开发，请务必注意参数类型是 Boolean ，不是字符串或其他
    }

    /**
     * 构建Android和IOS的自定义消息的推送消息对象（静默推送）
     *
     * @return PushPayload
     */
    @Override
    public PushResult buildCustomPushPayload(PushBean pushBean, String... alias) {

        return sendPush(PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
//                .setNotification(Notification.alert(pushBean.getAlert()))  //创建通知栏消息
                .setMessage(Message.newBuilder().setTitle(pushBean.getTitle()).setMsgContent(pushBean.getAlert()).addExtras(pushBean.getExtras()).build())
                .build());
    }

    /**
     * 调用api推送
     *
     * @param pushPayload 推送实体
     * @return
     */
    @Override
    public PushResult sendPush(PushPayload pushPayload) {
        log.info("发送极光推送请求: {}", pushPayload);
        PushResult result = null;
        try {
            String appKey = this.sysConfigFeign.getConfigKey(QueueConstants.J_PUSH_APP_KEY);
            String masterSecret = this.sysConfigFeign.getConfigKey(QueueConstants.J_PUSH_MASTER_SECRET);
            JPushClient jPushClient = new JPushClient(masterSecret, appKey);
            result = jPushClient.sendPush(pushPayload);
        } catch (Exception e) {
            log.error("极光推送失败：{}", e.getMessage());
        }
        return result;
    }

    /**
     * 获取统计数据
     */
    @Override
    public ReceivedsResult getReportReceived(String msgIds) {
        ReceivedsResult result = null;
        try {
            String appKey = this.sysConfigFeign.getConfigKey(QueueConstants.J_PUSH_APP_KEY);
            String masterSecret = this.sysConfigFeign.getConfigKey(QueueConstants.J_PUSH_MASTER_SECRET);
            String host = this.sysConfigFeign.getConfigKey(QueueConstants.J_PUSH_PROXY_HOST);
            String port = this.sysConfigFeign.getConfigKey(QueueConstants.J_PUSH_PROXY_PORT);
            HttpProxy httpProxy = new HttpProxy(host, NumberUtil.parseInt(port));
            ClientConfig config = ClientConfig.getInstance();
            JPushClient jPushClient = new JPushClient(masterSecret, appKey, httpProxy, config);
//        try {
            result = jPushClient.getReportReceiveds(msgIds);
        } catch (APIConnectionException e) {
            log.error("获取极光统计数据失败！", e.getMessage());

        } catch (APIRequestException e) {
            log.error("获取极光统计数据失败！", e.getErrorCode()+"---"+e.getErrorMessage());

        }
//        } catch (Exception e) {
//            log.error("获取极光统计数据失败！", e.getMessage());
//            log.error("获取极光统计数据失败！", e;
//        }
        return result;
    }

    /**
     * 标签推送
     *
     * @param pushBean
     * @param tag
     * @return
     */
    @Override
    public PushResult tagPushAll(PushBean pushBean, String... tag) {
        return sendPush(PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.tag(tag))
                .setMessage(Message.newBuilder().setTitle(pushBean.getTitle()).setMsgContent(pushBean.getAlert()).addExtras(pushBean.getExtras()).build())
                .build());
    }

}
