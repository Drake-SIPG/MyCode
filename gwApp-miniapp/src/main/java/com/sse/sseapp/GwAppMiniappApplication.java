package com.sse.sseapp;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wy
 * @date 2023-11-28
 */
@EnableFeignClients
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableSpringUtil
@EnableRetry
public class GwAppMiniappApplication {

    /**
     * 日志相关
     */
    private static Logger logger = LoggerFactory.getLogger(GwAppMiniappApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GwAppMiniappApplication.class, args);
        logger.info("miniapp模块启动成功！！!");
    }
}
