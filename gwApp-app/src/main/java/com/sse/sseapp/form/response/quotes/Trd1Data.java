package com.sse.sseapp.form.response.quotes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/7/5 14:19
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Trd1Data {
    /**
     * code : 600000
     * time : 162904
     * total : 2883
     * begin : 2871
     * end : 2883
     * trd1 : [[145609,7.25,200,1],[145612,7.26,200,0],[145615,7.26,700,0],[145618,7.25,1800,1],[145627,7.26,10000,0],[145636,7.26,3400,0],[145642,7.26,100,0],[145648,7.26,27100,0],[145651,7.26,21400,1],[145654,7.25,5100,1],[145700,7.27,100,0],[150003,7.25,618100,1]]
     */

    @JsonProperty("code")
    private String code;
    @JsonProperty("time")
    private Integer time;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("begin")
    private Integer begin;
    @JsonProperty("end")
    private Integer end;
    @JsonProperty("trd1")
    private List<List<String>> trd1;
}
