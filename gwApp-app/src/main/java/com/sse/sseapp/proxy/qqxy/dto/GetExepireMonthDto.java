package com.sse.sseapp.proxy.qqxy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/22 17:33 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GetExepireMonthDto {
    @JsonProperty("EXPIREMONTH")
    private String expiremonth;
    @JsonProperty("NAME")
    private String name;
}
