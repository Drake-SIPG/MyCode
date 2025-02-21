package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class FundQueryResBody  extends RespContentVO {
    @JsonProperty("CATEGORY")
    private String CATEGORY;
    @JsonProperty("COMPANY_NAME")
    private String COMPANY_NAME;
    @JsonProperty("FUND_ABBR")
    private String FUND_ABBR;
    @JsonProperty("FUND_CODE")
    private String FUND_CODE;
    @JsonProperty("FUND_EXPANSION_ABBR")
    private String FUND_EXPANSION_ABBR;
    @JsonProperty("INDEX_NAME")
    private String INDEX_NAME;
    @JsonProperty("LISTING_DATE")
    private String LISTING_DATE;
    @JsonProperty("SCALE")
    private String SCALE;
}
