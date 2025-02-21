package com.sse.sseapp.form.response;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
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
public class GetIpoListResBody extends RespContentVO {


    /**
     * offlinePlacingString : null
     * issuePrice : null
     * totalIssued : null
     * actualFundsRaised : null
     * listedDate : 20230412
     * publicFundsMedianPriceBef : null
     * paymentEndDate : 20220112
     * onlineIssuanceDate : 20220311
     * allocatedString : null
     * allocatedInvestorString : null
     * onlinePurchaseLimit : null
     * offlineIssuanceStartDate : null
     * publicFundsWapBef : null
     * ipoStatus : 0
     * announcementUrl : null
     * stockType : 0
     * offlineInvestorString : null
     * stockAbbrName : 2222
     * lotWinningRate : null
     * weightedAvgPriceBef : null
     * onlineCirculation : null
     * stockCode : 603002
     * announceSuccRateRsDate : null
     * issuancePriceEarningsRatio : null
     * medianPriceBefore : null
     * publicFundsMedianPriceAft : null
     * publicFundsWapAft : null
     * medianPriceAfter : null
     * securityExpandName : null
     * offlineCirculation : null
     * totalInitialIssue : null
     * offlineIssuanceEndDate : null
     * allotmentShares : null
     * paymentStartDate : null
     * weightedAvgPriceAft : null
     */

    private String offlinePlacingString;
    private String issuePrice;
    private String totalIssued;
    private String actualFundsRaised;
    private String listedDate;
    private String publicFundsMedianPriceBef;
    private String paymentEndDate;
    private String onlineIssuanceDate;
    private String allocatedString;
    private String allocatedInvestorString;
    private String onlinePurchaseLimit;
    private String offlineIssuanceStartDate;
    private String publicFundsWapBef;
    private String ipoStatus;
    private String announcementUrl;
    private String stockType;
    private String offlineInvestorString;
    private String stockAbbrName;
    private String lotWinningRate;
    private String weightedAvgPriceBef;
    private String onlineCirculation;
    private String stockCode;
    private String announceSuccRateRsDate;
    private String issuancePriceEarningsRatio;
    private String medianPriceBefore;
    private String publicFundsMedianPriceAft;
    private String publicFundsWapAft;
    private String medianPriceAfter;
    private String securityExpandName;
    private String offlineCirculation;
    private String totalInitialIssue;
    private String offlineIssuanceEndDate;
    private String allotmentShares;
    private String paymentStartDate;
    private String weightedAvgPriceAft;

    private String type;
    private String subType;

}
