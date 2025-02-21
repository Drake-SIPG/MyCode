package com.sse.sseapp.task;

import com.sse.sseapp.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 推送消息统计
 *
 * @author zhengyaosheng
 * @date 2023-03-09
 */
@Slf4j
@Service
@SuppressWarnings("Duplicates")
public class MessageStaTisTask {

    @Autowired
    private PushService pushService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * redis定时任务的 key
     */
    public static final String REDIS_TASK_JOB_LOCK_KEY = "REDIS_TASK_JOB_KEY";

    /**
     * redis定时任务的 value
     */
    public static final String REDIS_TASK_JOB_LOCK_VALUE = "REDIS_TASK_JOB_VALUE";

    /**
     * 推送消息统计接口
     */
    @Scheduled(cron = "0 0/15 * * * ? ")
    public void syncAppMessage() {
        // 定义加锁状态
        boolean lock = false;
        try {
            lock = redisTemplate.opsForValue().setIfAbsent(REDIS_TASK_JOB_LOCK_KEY, REDIS_TASK_JOB_LOCK_VALUE, 60, TimeUnit.SECONDS);
            if (lock) {
                log.info("定时任务---查询推送记录接口开始！");
                this.pushService.getPushRecord();
                log.info("定时任务---查询推送记录接口结束！");
            }
        } catch (Exception e) {
            log.error("定时任务---查询推送记录接口失败：{}", e.getMessage());
        } finally {
            //防止释放锁太快，加睡眠
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (lock) {
                // 释放锁
                redisTemplate.delete(REDIS_TASK_JOB_LOCK_KEY);
            }
        }
    }

}
