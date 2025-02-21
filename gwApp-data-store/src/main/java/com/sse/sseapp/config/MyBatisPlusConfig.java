package com.sse.sseapp.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by wangfeng on 2022/12/28 16:17.
 */
@Configuration
@EnableTransactionManagement
public class MyBatisPlusConfig {

    /**
     * 日志相关
     */
    private final static Logger logger = LoggerFactory.getLogger(MyBatisPlusConfig.class);
    // mybatis-plus-boot-starter upgrade to 3.5.3.1
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.OCEAN_BASE));
        return mybatisPlusInterceptor;
    }
//    /**
//     * 注册分页插件
//     * @return
//     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor(){
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        // 设置方言
//        paginationInterceptor.setDialectType("mysql");
//
//        return paginationInterceptor;
//    }
//
//    /**
//     * SQL执行效率插件
//     * @return
//     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor(){
//        return new PerformanceInterceptor();
//    }
//
//    /**
//     * 逻辑删除用，3.1.1之后的版本可不需要配置该bean，但项目这里用的是3.1.0的
//     *
//     * @return com.baomidou.mybatisplus.core.injector.ISqlInjector
//     */
//    @Bean
//    public ISqlInjector sqlInjector() {
//        return new LogicSqlInjector();
//    }
}
