package com.sse.sseapp.task;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.sse.sseapp.service.MiniAppService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 调用恒生云平台接口 判断服务是否存活
 *
 * @author wy
 * @date 2023-11-29
 */
@Component
@Slf4j
public class MiniAppSurvivalTask {

    @Autowired
    private MiniAppService miniAppService;

    @Scheduled(cron = "${miniapp.task_time}")
    public void task() {
        miniAppService.checkStatus();
    }
}
