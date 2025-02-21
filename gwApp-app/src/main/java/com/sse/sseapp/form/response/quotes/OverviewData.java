package com.sse.sseapp.form.response.quotes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/7/5 14:18
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OverviewData {
    /**
     * code : 600000
     * date : 20230413
     * time : 143314
     * snap : ["浦发银行",7.21,7.22,7.29,7.2,7.26,0.05,0.69,22238938,161015474,5900,12918277,9320661,[7.25,399000,7.24,455100,7.23,295600,7.22,366570,7.21,472800],[7.27,578697,7.28,652600,7.29,659000,7.3,803024,7.31,262100]]
     */
    @JsonProperty("code")
    private String code;
    @JsonProperty("date")
    private Integer date;
    @JsonProperty("time")
    private Integer time;
    @JsonProperty("snap")
    private List<Object> snap;
}
