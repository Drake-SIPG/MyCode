package com.sse.sseapp.service;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sse.sseapp.core.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ParameterConfig {

    @Autowired
    private ResourceLoader resourceLoader;

    @SneakyThrows
    public Map<String, String> getParameterConfig(String code) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        Resource resource = resourceLoader.getResource("classpath:parameterconfig.json");
        if (!resource.exists()) {
            return null;
        }
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        JsonNode jsonNode = objectMapper.readTree(resource.getInputStream());
        JsonNode value = jsonNode.get(code);
        if (value == null) {
            return null;
        }
        String json = value.toString();
        log.info("从parameterconfig中获取数据：{}", json);
        Map<String, String> map = JsonUtil.parseObject(json, Map.class);
        return map;
    }
}
