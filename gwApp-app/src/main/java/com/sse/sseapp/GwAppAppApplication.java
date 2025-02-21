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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableSpringUtil
@EnableRetry
@EnableAsync
public class GwAppAppApplication {

    /**
     * 日志相关
     */
    private static Logger logger = LoggerFactory.getLogger(GwAppAppApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GwAppAppApplication.class, args);
        //        ConfigurableApplicationContext context = SpringApplication.run(GwAppAppApplication.class, args);
        //        ProxyProvider proxyProvider = context.getBean(ProxyProvider.class);
        //        ISysProxyFeign proxyFeign = context.getBean(ISysProxyFeign.class);
        //
        //        login(proxyProvider, proxyFeign);
        //        getExepireMonth(proxyProvider, proxyFeign);

        logger.info("app模块启动成功！！!");
    }

//        private static void getExepireMonth(ProxyProvider proxyProvider, ISysProxyFeign proxyFeign) {
//            //期权学院
//            Map<String, Object> origin = ImmutableMap.of(
//                    "url", "/szqqxy-backtest-api/backtest/getExepireMonth",
//                    "startTime", "2018-03-01 00:00:00",
//                    "endTime", "2018-03-31 00:00:00"
//            );
//            ConcurrentHashMap<String, Object> content = MapUtil.newConcurrentHashMap(origin);
//            SysProxyConfig info = proxyFeign.getInfo("qqxy-getExepireMonth");
//            ProxyConfig proxyConfig = new ProxyConfig();
//            BeanUtil.copyProperties(info, proxyConfig);
//            proxyConfig.setData(content);
//            proxyConfig.setUuid("2333");
//            QqxyResponse<List<GetExepireMonthDto>> result = proxyProvider.proxy(proxyConfig, new TypeReference<QqxyResponse<List<GetExepireMonthDto>>>() {
//            });
//            logger.info(String.valueOf(result));
//            logger.info("app模块启动成功！！!");
//        }
    //
    //    private static void login(ProxyProvider proxyProvider, ISysProxyFeign proxyFeign) {
    //        //通用框架
    //        Map<String, Object> content = ImmutableMap.of(
    //                "loginName", "18317053485",
    //                "password", "Aaa111111"
    //        );
    //        SysProxyConfig info = proxyFeign.getInfo("cominfo-login");
    //        ProxyConfig proxyConfig = new ProxyConfig();
    //        BeanUtil.copyProperties(info, proxyConfig);
    //        proxyConfig.setData(content);
    //        proxyConfig.setUuid("2333");
    //        CominfoResponse<LoginDto> result = proxyProvider.proxy(proxyConfig, new TypeReference<CominfoResponse<LoginDto>>() {
    //        });
    //        logger.info(result.toString());
    //        logger.info("app模块启动成功！！!");
    //    }
}
