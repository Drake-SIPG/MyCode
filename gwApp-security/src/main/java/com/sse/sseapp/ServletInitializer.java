package com.sse.sseapp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/4/17 17:13 hanjian 创建
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(GwAppSecurityApplication.class);
    }

}
