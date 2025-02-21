package com.sse.sseapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GwAppSystemApplication {

    /**
     * 日志相关
     */
    private static Logger logger = LoggerFactory.getLogger(GwAppSystemApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GwAppSystemApplication.class, args);
        logger.info("系统管理模块启动成功！！!");
    }

}
