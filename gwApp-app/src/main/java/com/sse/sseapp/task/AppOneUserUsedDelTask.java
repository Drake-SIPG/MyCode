package com.sse.sseapp.task;

import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.domain.app.AppOneUserUsed;
import com.sse.sseapp.feign.app.IAppOneUserUsedFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 消息推送定时任务
 *
 * @author jiamingliang
 * @date 2023-07-20
 */
@Component
@Slf4j
public class AppOneUserUsedDelTask {

    @Autowired
    private IAppOneUserUsedFeign appOneUserUsedFeign;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 推送
     */
    @Scheduled(cron = "${ones.delused_time_task}")
    public void del() {

        // 定义加锁状态
        boolean lock = false;
        String redisKey = AppConstants.APP_ONE_USER_USED_DEL;
        try {
            lock = redisTemplate.opsForValue().setIfAbsent(redisKey, "删除用户最近使用", 60, TimeUnit.SECONDS);
            if (lock) {
                appOneUserUsedFeign.delete();
            }
        } catch (Exception e) {
            log.error("定时任务---删除用户使用失败：{}", e.getMessage());
        } finally {
            if (lock) {
                // 释放锁
                redisTemplate.delete(redisKey);
            }
        }
    }
}