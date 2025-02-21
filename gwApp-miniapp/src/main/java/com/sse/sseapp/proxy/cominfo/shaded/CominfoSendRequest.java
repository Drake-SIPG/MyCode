package com.sse.sseapp.proxy.cominfo.shaded;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;
import static cn.hutool.core.map.MapUtil.newHashMap;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/20 16:53 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CominfoSendRequest {
    @JsonProperty("reqContent")
    private Map<String, Object> reqContent = newHashMap();
    @JsonProperty("base")
    private BaseDTO base = new BaseDTO();
}
