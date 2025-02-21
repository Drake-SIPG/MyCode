package com.sse.sseapp;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
@MapperScan("com.sse.sseapp.mapper.*")
@SpringBootApplication
public class GwAppDataStoreApplication {

    private static final Logger logger = LoggerFactory.getLogger(GwAppDataStoreApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GwAppDataStoreApplication.class, args);
        logger.info("数据存储中心启动成功！！！");
    }

}
