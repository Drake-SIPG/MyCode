package com.sse.sseapp.form.response.quotes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/7/5 14:16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Result {
    @JsonProperty("date")
    private Integer date;
    @JsonProperty("time")
    private Integer time;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("begin")
    private Integer begin;
    @JsonProperty("end")
    private Integer end;
    @JsonProperty("list")
    private List<List<String>> list;
}
