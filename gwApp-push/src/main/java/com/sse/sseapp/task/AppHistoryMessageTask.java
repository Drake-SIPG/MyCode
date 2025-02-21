package com.sse.sseapp.task;

import cn.hutool.core.bean.BeanUtil;
import com.sse.sseapp.app.core.constant.AppPushMessageConstants;
import com.sse.sseapp.domain.push.AppPushMessage;
import com.sse.sseapp.domain.push.AppPushMessageHistory;
import com.sse.sseapp.feign.push.IAppPushConfigFeign;
import com.sse.sseapp.feign.push.IAppPushMessageFeign;
import com.sse.sseapp.feign.push.IAppPushMessageHistoryFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 消息推送定时任务
 *
 * @author wy
 * @date 2023-07-20
 */
@Component
@Slf4j
public class AppHistoryMessageTask {

    @Autowired
    private IAppPushMessageFeign appPushMessageFeign;

    @Autowired
    private IAppPushConfigFeign appPushConfigFeign;

    @Autowired
    private IAppPushMessageHistoryFeign appPushMessageHistoryFeign;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 转历史
     */
    @Scheduled(cron = "${PUSH.history_time_task}")
    public void convertHistory() {
        //消息保留天数
        String expirationTime = this.appPushConfigFeign.getConfigKey(AppPushMessageConstants.EXPIRATION_TIME);
        //获取需要转换历史数据
        List<AppPushMessage> list = appPushMessageFeign.historyList(expirationTime);
        for (AppPushMessage message : list) {
            // 定义加锁状态
            boolean lock = false;
            String redisKey = "appPushMessageHistory:" + message.getId();
            try {
                lock = redisTemplate.opsForValue().setIfAbsent(redisKey, "消息转历史", 60, TimeUnit.SECONDS);
                if (lock) {
                    AppPushMessageHistory history = new AppPushMessageHistory();
                    BeanUtil.copyProperties(message, history);
                    int row = appPushMessageHistoryFeign.add(history);
                    if (row > 0) {
                        log.info("消息转历史成功：{}", message);
                        //删除源数据
                        row = appPushMessageFeign.remove(message.getId());
                        if (row > 0) {
                            log.info("删除消息源数据成功：{}", message);
                        } else {
                            log.error("删除消息源数据失败：{}", message);
                        }
                    } else {
                        log.error("消息转历史失败：{}", message);
                    }
                }
            } catch (Exception e) {
                log.error("定时任务---消息转历史失败：{}", e.getMessage());
            } finally {
                //防止释放锁太快，加睡眠
                try {
                    Thread.sleep(60000);
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
