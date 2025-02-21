package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class MarketOverviewReqBody extends ReqContentVO {
    @JsonProperty("FUND_TYPE")
    private String FUND_TYPE;
    private String sqlId = "COMMON_JJZWZ_SY_JJSCZL";
}
