package com.sse.sseapp.jpush;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.google.gson.JsonObject;
import com.sse.sseapp.jpush.push.PushResult;
import com.sse.sseapp.jpush.push.model.Message;
import com.sse.sseapp.jpush.push.model.Options;
import com.sse.sseapp.jpush.push.model.Platform;
import com.sse.sseapp.jpush.push.model.PushPayload;
import com.sse.sseapp.jpush.push.model.audience.Audience;
import com.sse.sseapp.jpush.push.model.notification.AndroidNotification;
import com.sse.sseapp.jpush.push.model.notification.IosNotification;
import com.sse.sseapp.jpush.push.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.common.value.qual.BoolVal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author mateng
 * @since 2023/9/16 21:01
 */
@Component
@Slf4j
public class JpushUtil {

    static String apns;


    @Value("${PUSH.apnsProduction}")
    public  void setApns(String apn) {
        JpushUtil.apns= apn;
        log.info("apns-------"+apns);
    }


    /**
     * 推送给设备标识参数的用户
     *
     * @param registrationId    设备标识
     * @param notificationTitle 通知内容标题
     * @param msgTitle          消息内容标题
     * @param msgContent        消息内容
     * @param extrasparams      扩展字段
     * @return 0推送失败，1推送成功
     * 备注还可设置推送消息的角标 通知铃声等数据
     */
//    public static int sendToRegistrationId(String app_key,String master_secret,String registrationId, String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasparams) {
//        int result = 0;
//        try {
//            PushPayload pushPayload = JpushUtil.buildPushObjectAllRegistrationIdAlertWithTitle(registrationId, notificationTitle, msgTitle, msgContent, extrasparams);
//            System.out.println(pushPayload);
//            JPushClient jPushClient = new JPushClient(app_key, master_secret);
//            PushResult pushResult = jPushClient.sendPush(pushPayload);
//            System.out.println(pushResult);
//            if (pushResult.getResponseCode() == 200) {
//                result = 1;
//            }
//        } catch (APIConnectionException | APIRequestException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    /**
     * 发送给所有安卓用户
     *
     * @param notificationTitle 通知内容标题
     * @param msgTitle          消息内容标题
     * @param msgContent        消息内容
     * @param extrasparams      扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAllAndroid(String app_key, String master_secret, String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasparams) {

        int result = 0;
        try {
            PushPayload pushPayload = JpushUtil.buildPushObjectAndroidAllAlertWithTitle(notificationTitle, msgTitle, msgContent, extrasparams);
            System.out.println(pushPayload);
            JPushClient jPushClient = new JPushClient(app_key, master_secret);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            System.out.println(pushResult);
            if (pushResult.getResponseCode() == 200) {
                result = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送给所有IOS用户
     *
     * @param notificationTitle 通知内容标题
     * @param msgTitle          消息内容标题
     * @param msgContent        消息内容
     * @param extrasparam       扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAllIos(String app_key, String master_secret, String notificationTitle, String msgTitle, String msgContent, String extrasparam) {

        int result = 0;
        try {
            PushPayload pushPayload = JpushUtil.buildPushObjectIosAllAlertWithTitle(notificationTitle, msgTitle, msgContent, extrasparam);
            System.out.println(pushPayload);
            JPushClient jPushClient = new JPushClient(app_key, master_secret);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            System.out.println(pushResult);
            if (pushResult.getResponseCode() == 200) {
                result = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 发送给所有用户
     *
     * @param notificationTitle 通知内容标题
     * @param msgTitle          消息内容标题
     * @param msgContent        消息内容
     * @param extrasparams      扩展字段
     * @return 0推送失败，1推送成功
     */
//    public static int sendToAll(String app_key,String master_secret,String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasparams) {
//        int result = 0;
//        try {
//            PushPayload pushPayload = JpushUtil.buildPushObjectAndroidAndIos(notificationTitle, msgTitle, msgContent, extrasparams);
//            System.out.println(pushPayload);
//            JPushClient jPushClient = new JPushClient(app_key, master_secret);
//            PushResult pushResult = jPushClient.sendPush(pushPayload);
//            System.out.println(pushResult);
//            if (pushResult.getResponseCode() == 200) {
//                result = 1;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    /**
     * 推送给别名标识参数的用户
     *
     * @param bieming           别名
     * @param notificationTitle 通知内容标题
     * @param msgTitle          消息内容标题
     * @param msgContent        消息内容
     * @param extrasparams      扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToBieMing(String app_key, String master_secret, String bieming, String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasparams) {

        int result = 0;
        try {
            PushPayload pushPayload = JpushUtil.buildPushObjectAllBieMingAlertWithTitle(bieming, notificationTitle, msgTitle, msgContent, extrasparams);
            System.out.println(pushPayload);
            JPushClient jPushClient = new JPushClient(app_key, master_secret);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            System.out.println(pushResult);
            if (pushResult.getResponseCode() == 200) {
                result = 1;
            }
        } catch (APIConnectionException | APIRequestException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 给指定设备id推送所有设备
     *
     * @param registrationIds 设备id
     * @param extrasparams    附加字段
     * @return
     */
    public static PushPayload buildPushObjectAllRegistrationIdAlertWithTitle(String[] registrationIds, String notificationTitle, String notificationAlert, Map<String, String> extrasparams, String jpushConfig) {

        JsonObject intent = new JsonObject();
        //intent.addProperty("url", "intent:#Intent;action=com.hundsun.gmupush.jiguang;component=包名/com.hundsun.pushgmu.JiPushMessageActivity;end");
        intent.addProperty("url", jpushConfig);


        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台s
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.registrationId(registrationIds))
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(notificationAlert)
                                .setTitle(notificationTitle)
                                .setBadgeAddNum(0)
                                .setIntent(intent)
                                .addExtras(extrasparams)
                                .build())
                        //指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(notificationAlert)
                                .incrBadge(0)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtras(extrasparams)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                //.setContentAvailable(true)
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        // ios推送需要注意
                        .setApnsProduction(Boolean.parseBoolean(apns))
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(7200)
                        .build())
                .build();
    }

    /**
     * 给指定设备id推送所有设备消息
     *
     * @param registrationIds 设备id
     * @param msgTitle        消息标题
     * @param msgContent      消息内容
     * @param extrasparams    附加字段
     * @return
     */
    public static PushPayload buildPushObjectAllRegistrationIdMessageWithTitle(String[] registrationIds, String msgTitle, String msgContent, Map<String, String> extrasparams) {
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.registrationId(registrationIds))
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .addExtras(extrasparams)
                        .build())
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        // ios推送需要注意
                        .setApnsProduction(Boolean.parseBoolean(apns))
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(7200)
                        .build())
                .build();
    }


    /**
     * 给所有用户推送通知
     *
     * @param notificationTitle 通知标题
     * @param extrasparams      附加字段
     * @return
     */
    public static PushPayload buildPushObjectAlertAndroidAndIos(String notificationTitle, String notificationAlert, Map<String, String> extrasparams, String jpushConfig) {
        JsonObject intent = new JsonObject();
        intent.addProperty("url", jpushConfig);
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .setAlert(notificationAlert)
                        .addPlatformNotification(AndroidNotification.newBuilder()//android推送设置
                                .setAlert(notificationAlert)
                                .setTitle(notificationTitle)
                                .setIntent(intent)
                                //.setTitle(notificationTitle)//设置通知标题
                                .setBadgeAddNum(0)//设置角标添加数值
                                .addExtras(extrasparams)//设置附加字段map  也可以设置(key,value)格式
                                .build()
                        )
                        .addPlatformNotification(IosNotification.newBuilder()//ios推送设置
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(notificationAlert)
                                //此项是指定此推送的badge自动加1
                                .incrBadge(0)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                .addExtras(extrasparams)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                // .setContentAvailable(true)
                                .build()
                        )
                        .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(Boolean.parseBoolean(apns))
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        //.setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(7200)
                        .build()
                )
                .build();
    }

    /**
     * 给所有用户推送消息
     *
     * @param msgTitle     内容标题
     * @param msgContent   推送内容
     * @param extrasparams 附加字段
     * @return
     */
    public static PushPayload buildPushObjectMessageAndroidAndIos(String msgTitle, String msgContent, Map<String, String> extrasparams) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)//通知内容
                        .setTitle(msgTitle)//通知内容标题
                        .build())
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(Boolean.parseBoolean(apns))
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        //.setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(7200)
                        .build()
                )
                .build();
    }

    /**
     * 别名
     *
     * @param bieming           别名
     * @param notificationTitle 通知标题
     * @param msgTitle          内容标题
     * @param msgContent        推送内容
     * @param extrasparams      附加字段
     * @return
     */
    public static PushPayload buildPushObjectAllBieMingAlertWithTitle(String bieming, String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasparams) {
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.alias(bieming))
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(notificationTitle)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("sound.caf")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtras(extrasparams)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)
                                .build())
                        .build())
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                //消息
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .build())
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(Boolean.parseBoolean(apns))
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        //.setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(7200)
                        .build())
                .build();
    }

    public static PushPayload buildPushObjectAndroidAllAlertWithTitle(String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasparams) {
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.android())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(notificationTitle)
                                .setTitle(notificationTitle)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtras(extrasparams)
                                .build())
                        .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .addExtras(extrasparams)
                        .build())
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(Boolean.parseBoolean(apns))
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        //.setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(7200)
                        .build())
                .build();
    }

    public static PushPayload buildPushObjectIosAllAlertWithTitle(String notificationTitle, String msgTitle, String msgContent, String extrasparam) {
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.ios())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(notificationTitle)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("sound.caf")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                // .setContentAvailable(true)
                                .build())
                        .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .setTitle(msgTitle)
                        .addExtra("message extras key", extrasparam)
                        .build())
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(Boolean.parseBoolean(apns))
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(7200)
                        .build())
                .build();
    }

}
