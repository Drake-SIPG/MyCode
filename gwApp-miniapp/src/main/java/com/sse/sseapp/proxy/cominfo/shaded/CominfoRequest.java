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
public class CominfoRequest {
    @JsonProperty("base")
    private BaseDTOX base = new BaseDTOX();

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class BaseDTOX {
        @JsonProperty("dataParam")
        private DataParamDTO dataParam = new DataParamDTO();
        @JsonProperty("appBundle")
        private String appBundle;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Data
        public static class DataParamDTO {
            @JsonProperty("reqContent")
            private Map<String, Object> reqContent = newHashMap();
            @JsonProperty("base")
            private BaseDTO base = new BaseDTO();
        }
    }
}
