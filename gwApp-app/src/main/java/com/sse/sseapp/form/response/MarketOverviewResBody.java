package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class MarketOverviewResBody extends RespContentVO {
    @JsonProperty("TRADE_DATE")
    private String TRADE_DATE;
    @JsonProperty("SCALE")
    private String SCALE;
    @JsonProperty("TRADE_VOL_YEAR")
    private String TRADE_VOL_YEAR;
    @JsonProperty("TRADE_AMT_YEAR")
    private String TRADE_AMT_YEAR;
    @JsonProperty("LIST_NUM")
    private String LIST_NUM;
    @JsonProperty("FUND_TYPE")
    private String FUND_TYPE;
    @JsonProperty("FUND_ABBR")
    private String FUND_ABBR;
}
