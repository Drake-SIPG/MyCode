package com.sse.sseapp.proxy.qqxy;

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
public class QqxyResponse<T> {
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("nonBizError")
    private Boolean nonBizError;
    @JsonProperty("data")
    private T data;
}
