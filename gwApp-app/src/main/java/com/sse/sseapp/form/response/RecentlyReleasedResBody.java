package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class RecentlyReleasedResBody extends RespContentVO {
    @JsonProperty("COMPANY_NAME")
    private String COMPANY_NAME;
    @JsonProperty("FUND_CODE")
    private String FUND_CODE;
    @JsonProperty("FUND_EXPANSION_ABBR")
    private String FUND_EXPANSION_ABBR;
    @JsonProperty("EXPRESS_CODE")
    private String EXPRESS_CODE;
    @JsonProperty("FUND_DETAILS")
    private String FUND_DETAILS;
    @JsonProperty("LISTING_DATE")
    private String LISTING_DATE;
}
