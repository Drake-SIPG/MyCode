package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ValueRankingResBody extends RespContentVO {
    @JsonProperty("RN")
    private String RN;
    @JsonProperty("SEC_CODE")
    private String SEC_CODE;
    @JsonProperty("SEC_NAME")
    private String SEC_NAME;
    @JsonProperty("SECURITY_ABBR_FULL")
    private String SECURITY_ABBR_FULL;
    @JsonProperty("TOTAL_VALUE")
    private String TOTAL_VALUE;
    @JsonProperty("TOTAL_VALUE_RATIO")
    private String TOTAL_VALUE_RATIO;
    @JsonProperty("TOTAL_VALUE_PER")
    private String TOTAL_VALUE_PER;
    @JsonProperty("NEGO_VALUE")
    private String NEGO_VALUE;
    @JsonProperty("NEGO_VALUE_RATIO")
    private String NEGO_VALUE_RATIO;
    @JsonProperty("NEGO_VALUE_PER")
    private String NEGO_VALUE_PER;
    @JsonProperty("TRADE_DATE")
    private String TRADE_DATE;
}
