package com.sse.sseapp.proxy.cominfo.shaded;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/20 16:53 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CominfoEncryptedSendRequest {
    @JsonProperty("base")
    private BaseDTO base = new BaseDTO();

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class BaseDTO {
        @JsonProperty("dataParam")
        private String dataParam = "";
        @JsonProperty("appBundle")
        private String appBundle = "com.sse.ssegwapp";
    }
}
