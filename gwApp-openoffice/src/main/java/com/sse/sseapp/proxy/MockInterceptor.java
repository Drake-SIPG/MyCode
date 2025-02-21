package com.sse.sseapp.proxy;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/30 11:42 hanjian 创建
 */
@Component
@Slf4j
public class MockInterceptor {
    @SneakyThrows
    public <T> T mock(ProxyConfig config, TypeReference<T> type) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        String userDir = context.getEnvironment().getProperty("user.dir");
        String path = userDir + "/mock.json";
        Resource resource = context.getResource("file:" + path);
        if (!resource.exists()) {
            return null;
        }
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        JsonNode jsonNode = objectMapper.readTree(resource.getInputStream());
        JsonNode value = jsonNode.get(config.getCode());
        if (value == null) {
            return null;
        }
        String json = value.toString();
        log.info("从mock获取数据：{}", json);
        return objectMapper.readValue(json, type);
    }
}
