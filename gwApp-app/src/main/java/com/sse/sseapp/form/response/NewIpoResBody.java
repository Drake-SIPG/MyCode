package com.sse.sseapp.form.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class NewIpoResBody extends RespContentVO {
    @JsonProperty("announceSuccRateRsDate")
    private String announceSuccRateRsDate;
    @JsonProperty("ipoStatus")
    private String ipoStatus;
    @JsonProperty("issuancePriceEarningsRatio")
    private Double issuancePriceEarningsRatio;
    @JsonProperty("issuePrice")
    private Double issuePrice;
    @JsonProperty("lotWinningRate")
    private Double lotWinningRate;
    @JsonProperty("offlineCirculation")
    private String offlineCirculation;
    @JsonProperty("offlineIssuanceEndDate")
    private String offlineIssuanceEndDate;
    @JsonProperty("offlineIssuanceStartDate")
    private String offlineIssuanceStartDate;
    @JsonProperty("onlineCirculation")
    private String onlineCirculation;
    @JsonProperty("onlineIssuanceDate")
    private String onlineIssuanceDate;
    @JsonProperty("onlinePurchaseLimit")
    private String onlinePurchaseLimit;
    @JsonProperty("paymentEndDate")
    private String paymentEndDate;
    @JsonProperty("paymentStartDate")
    private String paymentStartDate;
    @JsonProperty("securityExpandName")
    private String securityExpandName;
    @JsonProperty("stockAbbrName")
    private String stockAbbrName;
    @JsonProperty("stockCode")
    private String stockCode;
    @JsonProperty("stockType")
    private String stockType;
    @JsonProperty("totalInitialIssue")
    private String totalInitialIssue;
    @JsonProperty("totalIssued")
    private String totalIssued;
    private Boolean isSelf = false;

}
