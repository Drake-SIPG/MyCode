package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class DailyStatisticsResBody extends RespContentVO {
    @JsonProperty("CALL_VOLUME")
    private String CALL_VOLUME;
    @JsonProperty("CP_RATE")
    private String CP_RATE;
    @JsonProperty("LEAVES_CALL_QTY")
    private String LEAVES_CALL_QTY;
    @JsonProperty("LEAVES_PUT_QTY")
    private String LEAVES_PUT_QTY;
    @JsonProperty("LEAVES_QTY")
    private String LEAVES_QTY;
    @JsonProperty("PUT_VOLUME")
    private String PUT_VOLUME;
    @JsonProperty("SECURITY_ABBR")
    private String SECURITY_ABBR;
    @JsonProperty("SECURITY_CODE")
    private String SECURITY_CODE;
    @JsonProperty("TOTAL_VOLUME")
    private String TOTAL_VOLUME;
    @JsonProperty("TRADE_DATE")
    private String TRADE_DATE;
}
