package com.sse.sseapp.proxy.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/4/4 14:27 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SearchDataDto {
    @JsonProperty("channelCode")
    private String channelCode;
    @JsonProperty("count")
    private String count;
    @JsonProperty("countPage")
    private String countPage;
    @JsonProperty("data")
    private String data;
    @JsonProperty("orderby")
    private String orderby;
    @JsonProperty("page")
    private String page;
    @JsonProperty("pageRequest")
    private Integer pageRequest;
    @JsonProperty("perpage")
    private String perpage;
    @JsonProperty("question")
    private String question;
    @JsonProperty("search")
    private String search;
    @JsonProperty("searchTime")
    private String searchTime;
    @JsonProperty("token")
    private String token;
}
