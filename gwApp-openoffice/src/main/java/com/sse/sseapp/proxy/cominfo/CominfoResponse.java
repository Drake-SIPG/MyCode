package com.sse.sseapp.proxy.cominfo;

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
public class CominfoResponse<T> {
    @JsonProperty("state")
    private Integer state;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private T data;
}
