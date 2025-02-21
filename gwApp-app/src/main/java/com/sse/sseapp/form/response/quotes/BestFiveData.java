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
public class BestFiveData {
    /**
     * code : 600000
     * date : 20230413
     * time : 160206
     * snap : ["浦发银行",7.21,7.22,7.29,7.2,7.25,0.04,0.55,25779019,186717624,618100,14538802,11240217,[7.25,260400,7.24,416200,7.23,365700,7.22,383470,7.21,412300],[7.26,424300,7.27,234400,7.28,912500,7.29,630000,7.3,832924]]
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
