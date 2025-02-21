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
public class Trd2Data {
    /**
     * code : 600000
     * time : 162904
     * trd2 : [[7.29,221200],[7.28,1322746],[7.27,2806963],[7.26,4614230],[7.25,3362030],[7.24,4566473],[7.23,3119652],[7.22,3575231],[7.21,1701994],[7.2,488500]]
     */
    @JsonProperty("code")
    private String code;
    @JsonProperty("time")
    private Integer time;
    @JsonProperty("trd2")
    private List<List<String>> trd2;
}
