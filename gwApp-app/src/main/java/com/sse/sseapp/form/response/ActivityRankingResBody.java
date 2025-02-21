package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ActivityRankingResBody extends RespContentVO {
    @JsonProperty("RN")
    private String RN;
    @JsonProperty("SEC_CODE")
    private String SEC_CODE;
    @JsonProperty("SEC_NAME")
    private String SEC_NAME;
    @JsonProperty("OPEN_PRICE")
    private String OPEN_PRICE;
    @JsonProperty("QJZF")
    private String QJZF;
    @JsonProperty("CLOSE_PRICE")
    private String CLOSE_PRICE;
    @JsonProperty("TRADE_AMT")
    private String TRADE_AMT;
    @JsonProperty("NUM")
    private String NUM;
    @JsonProperty("CHANGE_RATIO")
    private String CHANGE_RATIO;
    @JsonProperty("TRADE_VOL")
    private String TRADE_VOL;
    @JsonProperty("AVG_PRICE")
    private String AVG_PRICE;
    @JsonProperty("SECURITY_ABBR_FULL")
    private String SECURITY_ABBR_FULL;
    @JsonProperty("TO_RATE")
    private String TO_RATE;
    @JsonProperty("TRADE_DATE")
    private String TRADE_DATE;
}
