package com.sse.sseapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class GwAppPushApplication {
    /**
     * 日志相关
     */
    private static Logger logger = LoggerFactory.getLogger(GwAppPushApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GwAppPushApplication.class, args);
        logger.info("推送模块启动成功！！!");
    }

}
