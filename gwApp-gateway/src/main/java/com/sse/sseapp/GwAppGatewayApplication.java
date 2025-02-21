package com.sse.sseapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GwAppGatewayApplication {

    private static final Logger logger = LoggerFactory.getLogger(GwAppGatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GwAppGatewayApplication.class, args);
        logger.info("GateWay启动成功！！！");
    }

}
