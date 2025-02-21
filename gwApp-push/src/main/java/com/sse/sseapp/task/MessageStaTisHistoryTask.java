package com.sse.sseapp.task;

import cn.hutool.core.bean.BeanUtil;
import com.sse.sseapp.app.core.constant.AppPushMessageConstants;
import com.sse.sseapp.domain.push.AppMessagePushRecord;
import com.sse.sseapp.domain.push.AppMessagePushRecordHistory;
import com.sse.sseapp.feign.push.IAppMessagePushRecordFeign;
import com.sse.sseapp.feign.push.IAppMessagePushRecordHistoryFeign;
import com.sse.sseapp.feign.push.IAppPushConfigFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 推送消息统计历史记录
 *
 * @author wy
 * @date 2023-07-25
 */
@Slf4j
@Service
public class MessageStaTisHistoryTask {

    @Autowired
    private IAppPushConfigFeign appPushConfigFeign;

    @Autowired
    private IAppMessagePushRecordFeign appMessagePushRecordFeign;

    @Autowired
    private IAppMessagePushRecordHistoryFeign appMessagePushRecordHistoryFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 推送消息统计接口
     */
    @Scheduled(cron = "${PUSH.history_time_task}")
    public void convertHistory() {
        //消息保留天数
        String expirationTime = this.appPushConfigFeign.getConfigKey(AppPushMessageConstants.EXPIRATION_TIME);
        //获取需要转换历史数据
        List<AppMessagePushRecord> list = appMessagePushRecordFeign.historyList(expirationTime);
        for (AppMessagePushRecord record : list) {
            // 定义加锁状态
            boolean lock = false;
            String redisKey = "appMessagePushRecordHistory:" + record.getId();
            try {
                lock = redisTemplate.opsForValue().setIfAbsent(redisKey, "推送结果信息记录转历史");
                if (lock) {
                    AppMessagePushRecordHistory history = new AppMessagePushRecordHistory();
                    BeanUtil.copyProperties(record, history);
                    int row = appMessagePushRecordHistoryFeign.add(history);
                    if (row > 0) {
                        log.info("推送结果信息记录转历史成功：{}", record);
                        //删除源数据
                        row = appMessagePushRecordFeign.remove(record.getId());
                        if (row > 0) {
                            log.info("删除推送结果信息源数据成功：{}", record);
                        } else {
                            log.error("删除推送结果信息源数据成功：{}", record);
                        }
                    } else {
                        log.error("推送结果信息记录转历史失败：{}", record);
                    }
                }
            } catch (Exception e) {
                log.error("定时任务---推送结果信息记录转历史失败：{}", e.getMessage());
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
