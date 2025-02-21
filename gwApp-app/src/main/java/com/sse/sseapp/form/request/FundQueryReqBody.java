package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class FundQueryReqBody extends ReqContentVO {
    @JsonProperty("FUND_CODE")
    private String FUND_CODE;
    @JsonProperty("COMPANY_NAME")
    private String COMPANY_NAME;
    @JsonProperty("INDEX_NAME")
    private String INDEX_NAME;
    @JsonProperty("START_DATE")
    private String START_DATE;
    @JsonProperty("END_DATE")
    private String END_DATE;
    @JsonProperty("CATEGORY")
    private String CATEGORY;
    @JsonProperty("SUBCLASS")
    private String SUBCLASS;
    @JsonProperty("SWING_TRADE")
    private String SWING_TRADE;
    private String type = "inParams";
    @JsonProperty("FUND_CODE_ASC")
    private String FUND_CODE_ASC;
    @JsonProperty("FUND_CODE_DESC")
    private String FUND_CODE_DESC;
    @JsonProperty("INDEX_NAME_ASC")
    private String INDEX_NAME_ASC;
    @JsonProperty("INDEX_NAME_DESC")
    private String INDEX_NAME_DESC;
    @JsonProperty("LISTING_DATE_ASC")
    private String LISTING_DATE_ASC;
    @JsonProperty("LISTING_DATE_DESC")
    private String LISTING_DATE_DESC;
    @JsonProperty("SCALE_ASC")
    private String SCALE_ASC;
    @JsonProperty("SCALE_DESC")
    private String SCALE_DESC;
    @JsonProperty("COMPANY_NAME_ASC")
    private String COMPANY_NAME_ASC;
    @JsonProperty("COMPANY_NAME_DESC")
    private String COMPANY_NAME_DESC;
    @JsonProperty("CATEGORY_ASC")
    private String CATEGORY_ASC;
    private String sqlId = "COMMON_JJZWZ_JJLB_L";
}
