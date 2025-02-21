package com.sse.sseapp.props;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import static com.google.common.collect.Lists.newArrayList;

@ConfigurationProperties("dynamic.uri")
@RefreshScope
@Component
@Data
public class DynamicUriProperties {
    private List<String> whiteList = newArrayList();
}
