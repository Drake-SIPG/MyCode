package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 行情走势数据
 * @author mateng
 */
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
@Data
public class StockLineResBody extends RespContentVO {

    @JsonProperty("code")
    private String code;

    @JsonProperty("last")
    private String last;

    @JsonProperty("open")
    private String open;


    @JsonProperty("prev_close")
    private String prev_close;

    @JsonProperty("highest")
    private String highest;

    @JsonProperty("lowest")
    private String lowest;

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

    @JsonProperty("line")
    private List<List<String>> line;

    @JsonProperty("xArr")
    private List<String> xArr;
}
