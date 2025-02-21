package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * k线数据
 */
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
@Data
public class StockKLineResBody extends RespContentVO {

    @JsonProperty("code")
    private String code;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("begin")
    private Integer begin;

    @JsonProperty("end")
    private Integer end;

    @JsonProperty("kline")
    private List<List<String>> kline;
}
