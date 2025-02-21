package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class DailyFundOverviewResBody extends RespContentVO {
    @JsonProperty("PRODUCT_CODE")
    private String PRODUCT_CODE;
    @JsonProperty("TRADE_VOL")
    private String TRADE_VOL;
    @JsonProperty("TRADE_AMT")
    private String TRADE_AMT;
    @JsonProperty("TRADE_DATE")
    private String TRADE_DATE;
    @JsonProperty("SCALE")
    private String SCALE;
    @JsonProperty("LIST_NUM")
    private String LIST_NUM;
}
