package com.sse.sseapp.task;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.connection.HttpProxy;
import com.sse.sseapp.app.core.constant.AppPushMessageConstants;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.constants.QueueConstants;
import com.sse.sseapp.domain.app.AppMessageDevice;
import com.sse.sseapp.domain.push.AppPushMessage;
import com.sse.sseapp.feign.app.IAppMessageDeviceFeign;
import com.sse.sseapp.feign.push.IAppPushConfigFeign;
import com.sse.sseapp.feign.push.IAppPushMessageFeign;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.jpush.JPushClient;
import com.sse.sseapp.jpush.JpushUtil;
import com.sse.sseapp.jpush.push.PushResult;
import com.sse.sseapp.jpush.push.model.PushPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 消息推送定时任务
 *
 * @author wy
 * @date 2023-07-20
 */
@Component
@Slf4j
public class AppPushMessageTask {

    @Autowired
    private IAppPushMessageFeign appPushMessageFeign;

    @Autowired
    private IAppPushConfigFeign appPushConfigFeign;

    @Autowired
    private ISysConfigFeign sysConfigFeign;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IAppMessageDeviceFeign appMessageDeviceFeign;

    @Value("${PUSH.jpush_config}")
    private String jpushConfig;

    /**
     * 推送
     */
    @Scheduled(cron = "${PUSH.push_time_task}")
    public void push() {
        // 判断推送开关是否开启
        String pushSwitch = this.appPushConfigFeign.getConfigKey(AppPushMessageConstants.PUSH_SWITCH);
        if (!Boolean.parseBoolean(pushSwitch)) {
            return;
        }
        //推送次数
        Integer pushNumber = Integer.valueOf(this.appPushConfigFeign.getConfigKey(AppPushMessageConstants.PUSH_NUMBER));
        //获取推送列表
        List<AppPushMessage> list = appPushMessageFeign.pushList();
        for (AppPushMessage message : list) {
            log.info("推送内容: {}", message);
            // 定义加锁状态
            boolean lock = false;
            String redisKey = "appPushMessage:" + message.getId();
            Integer failedNumber = message.getFailNumber();
            try {
                lock = redisTemplate.opsForValue().setIfAbsent(redisKey, "消息推送", 60, TimeUnit.SECONDS);
                if (lock) {
                    if (message.getFailNumber() > pushNumber) {
                        //更改状态为作废
                        AppPushMessage appPushMessage = new AppPushMessage();
                        appPushMessage.setId(message.getId());
                        appPushMessage.setPublishStatus(AppPushMessageConstants.APP_PUSH_MASSAGE_STATUS_REPEAL);
                        appPushMessageFeign.edit(appPushMessage);
                    } else {
                        //推送
                        PushPayload pushPayload;
                        Map<String, String> extras = new HashMap<>();
                        extras.put("clickType", message.getClickType());
                        extras.put("clickUrl", message.getClickUrl());
                        extras.put("standaloneMode", message.getStandaloneMode().toString());
                        String msgType = message.getMsgType();
                        List<String> registrationIds = new ArrayList<>();
                        if (ObjectUtil.isNotEmpty(message.getPhoneNo())) {
                            // TODO 根据手机号找设备号
                            List<AppMessageDevice> messageDeviceList = appMessageDeviceFeign.list();
                            for (int i = 0; i < message.getPhoneNo().split("\\|").length; i++) {
                                String phoneNo = message.getPhoneNo().split("\\|")[i];
                                Optional<String> optional = messageDeviceList.stream().filter(a -> ObjectUtil.equal(a.getMobile(), phoneNo)).map(AppMessageDevice::getMessageId).findFirst();
                                optional.ifPresent(registrationIds::add);
                            }
                            //查询用户未找到
                            if (registrationIds.size() == 0) {
                                throw new AppException("根据推送要求找不到指定用户登陆设备信息");
//                                break;
                            }
                        }
                        String appKey = this.sysConfigFeign.getConfigKey(QueueConstants.J_PUSH_APP_KEY);
                        String masterSecret = this.sysConfigFeign.getConfigKey(QueueConstants.J_PUSH_MASTER_SECRET);
                        String host = this.sysConfigFeign.getConfigKey(QueueConstants.J_PUSH_PROXY_HOST);
                        String port = this.sysConfigFeign.getConfigKey(QueueConstants.J_PUSH_PROXY_PORT);
//                        log.info("appKey: " + appKey + ", masterSecret: " + masterSecret);
//                        log.info("host: " + host + ", port: " + port);
                        HttpProxy httpProxy = new HttpProxy(host, NumberUtil.parseInt(port));
                        ClientConfig config = ClientConfig.getInstance();
                        config.setMaxRetryTimes(3);
                        //config.setPushHostName("http://" + host + ":" + port);
                        JPushClient jPushClient = new JPushClient(masterSecret, appKey, httpProxy, config);
//                        JPushClient jPushClient = new JPushClient(masterSecret, appKey, null, config);
                        PushResult result;
                        PushPayload payload;
                        if ("1".equals(msgType)) { //通知栏消息
                            if (registrationIds.size() > 0) {
                                payload = JpushUtil.buildPushObjectAllRegistrationIdAlertWithTitle(ArrayUtil.toArray(registrationIds,String.class), message.getTitle(), message.getContent(), extras, jpushConfig);
                            } else {
                                payload = JpushUtil.buildPushObjectAlertAndroidAndIos(message.getTitle(), message.getContent(), extras, jpushConfig);
                            }
                        } else { // app内消息
                            if (registrationIds.size() > 0) {
                                payload = JpushUtil.buildPushObjectAllRegistrationIdMessageWithTitle(ArrayUtil.toArray(registrationIds,String.class), message.getTitle(), message.getContent(), extras);
                            } else {
                                payload = JpushUtil.buildPushObjectMessageAndroidAndIos(message.getTitle(), message.getContent(), extras);
                            }
                        }
                        result = jPushClient.sendPush(payload);

                        if (ObjectUtil.equal(result.statusCode, 0)) {
                            log.info("极光推送消息成功，id: {}, result: {}", message.getId(), result);
                            // 发送成功
                            AppPushMessage appPushMessage = new AppPushMessage();
                            appPushMessage.setId(message.getId());
                            appPushMessage.setPublishStatus(AppPushMessageConstants.APP_PUSH_MASSAGE_STATUS_SUCCESS);
                            appPushMessage.setMsgId(String.valueOf(result.msg_id));
                            appPushMessage.setSendNo(String.valueOf(result.sendno));
                            appPushMessageFeign.edit(appPushMessage);
                        } else {
                            log.error("极光推送消息失败，id: {}, result: {}", message.getId(), result);
                            // 发送失败
                            AppPushMessage appPushMessage = new AppPushMessage();
                            appPushMessage.setId(message.getId());
                            appPushMessage.setPublishStatus(AppPushMessageConstants.APP_PUSH_MASSAGE_STATUS_FAIL);
                            failedNumber = failedNumber + 1;
                            // 更新失败次数
                            appPushMessage.setFailNumber(failedNumber);
                            appPushMessageFeign.edit(appPushMessage);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("定时任务---极光推送失败：{}", e.getMessage(), e);
                AppPushMessage appPushMessage = new AppPushMessage();
                appPushMessage.setId(message.getId());
                appPushMessage.setPublishStatus(AppPushMessageConstants.APP_PUSH_MASSAGE_STATUS_FAIL);
                failedNumber = failedNumber + 1;
                appPushMessage.setFailNumber(failedNumber);
                appPushMessageFeign.edit(appPushMessage);
            } finally {
                //防止释放锁太快，加睡眠
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (lock) {
                    // 释放锁
                    redisTemplate.delete(redisKey);
                }
            }
        }
    }

}
