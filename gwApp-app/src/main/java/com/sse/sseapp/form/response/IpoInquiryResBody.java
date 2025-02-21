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
public class IpoInquiryResBody extends RespContentVO {


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
    private String offlineInvestorObject;
    private String allocatedInvestorObject;
    private String allocatedObject;
    private String offlinePlacingObject;

    @JsonGetter("OFFLINE_PLACING_OBJECT")
    public String getOfflinePlacingObject() {
        return offlinePlacingObject;
    }

    @JsonSetter("offlinePlacingObject")
    public void setOfflinePlacingObject(String offlinePlacingObject) {
        this.offlinePlacingObject = offlinePlacingObject;
    }

    @JsonGetter("ALLOCATED_OBJECT")
    public String getAllocatedObject() {
        return allocatedObject;
    }

    @JsonSetter("allocatedObject")
    public void setAllocatedObject(String allocatedObject) {
        this.allocatedObject = allocatedObject;
    }

    @JsonGetter("OFFLINE_INVESTOR_OBJECT")
    public String getOfflineInvestorObject() {
        return offlineInvestorObject;
    }

    @JsonSetter("offlineInvestorObject")
    public void setOfflineInvestorObject(String offlineInvestorObject) {
        this.offlineInvestorObject = offlineInvestorObject;
    }

    @JsonGetter("ALLOCATED_INVESTOR_OBJECT")
    public String getAllocatedInvestorObject() {
        return allocatedInvestorObject;
    }

    @JsonSetter("allocatedInvestorObject")
    public void setAllocatedInvestorObject(String allocatedInvestorObject) {
        this.allocatedInvestorObject = allocatedInvestorObject;
    }

    @JsonGetter("offlinePlacingString")
    public String getOfflinePlacingString() {
        return offlinePlacingString;
    }

    @JsonSetter("offlinePlacingString")
    public void setOfflinePlacingString(String offlinePlacingString) {
        this.offlinePlacingString = offlinePlacingString;
    }

    @JsonGetter("ISSUE_PRICE")
    public String getIssuePrice() {
        return issuePrice;
    }

    @JsonSetter("issuePrice")
    public void setIssuePrice(String issuePrice) {
        this.issuePrice = issuePrice;
    }

    @JsonGetter("totalIssued")
    public String getTotalIssued() {
        return totalIssued;
    }

    @JsonSetter("totalIssued")
    public void setTotalIssued(String totalIssued) {
        this.totalIssued = totalIssued;
    }

    @JsonGetter("actualFundsRaised")
    public String getActualFundsRaised() {
        return actualFundsRaised;
    }

    @JsonSetter("actualFundsRaised")
    public void setActualFundsRaised(String actualFundsRaised) {
        this.actualFundsRaised = actualFundsRaised;
    }

    @JsonGetter("listedDate")
    public String getListedDate() {
        return listedDate;
    }

    @JsonSetter("listedDate")
    public void setListedDate(String listedDate) {
        this.listedDate = listedDate;
    }

    @JsonGetter("PUBLIC_FUNDS_MEDIAN_PRICE_BEF")
    public String getPublicFundsMedianPriceBef() {
        return publicFundsMedianPriceBef;
    }

    @JsonSetter("publicFundsMedianPriceBef")
    public void setPublicFundsMedianPriceBef(String publicFundsMedianPriceBef) {
        this.publicFundsMedianPriceBef = publicFundsMedianPriceBef;
    }

    @JsonGetter("PAYMENT_END_DATE")
    public String getPaymentEndDate() {
        return paymentEndDate;
    }

    @JsonSetter("paymentEndDate")
    public void setPaymentEndDate(String paymentEndDate) {
        this.paymentEndDate = paymentEndDate;
    }

    @JsonGetter("ONLINE_ISSUANCE_DATE")
    public String getOnlineIssuanceDate() {
        return onlineIssuanceDate;
    }

    @JsonSetter("onlineIssuanceDate")
    public void setOnlineIssuanceDate(String onlineIssuanceDate) {
        this.onlineIssuanceDate = onlineIssuanceDate;
    }

    @JsonGetter("allocatedString")
    public String getAllocatedString() {
        return allocatedString;
    }

    @JsonSetter("allocatedString")
    public void setAllocatedString(String allocatedString) {
        this.allocatedString = allocatedString;
    }

    @JsonGetter("allocatedInvestorString")
    public String getAllocatedInvestorString() {
        return allocatedInvestorString;
    }

    @JsonSetter("allocatedInvestorString")
    public void setAllocatedInvestorString(String allocatedInvestorString) {
        this.allocatedInvestorString = allocatedInvestorString;
    }

    @JsonGetter("onlinePurchaseLimit")
    public String getOnlinePurchaseLimit() {
        return onlinePurchaseLimit;
    }

    @JsonSetter("onlinePurchaseLimit")
    public void setOnlinePurchaseLimit(String onlinePurchaseLimit) {
        this.onlinePurchaseLimit = onlinePurchaseLimit;
    }

    @JsonGetter("OFFLINE_ISSUANCE_START_DATE")
    public String getOfflineIssuanceStartDate() {
        return offlineIssuanceStartDate;
    }

    @JsonSetter("offlineIssuanceStartDate")
    public void setOfflineIssuanceStartDate(String offlineIssuanceStartDate) {
        this.offlineIssuanceStartDate = offlineIssuanceStartDate;
    }

    @JsonGetter("PUBLIC_FUNDS_WAP_BEF")
    public String getPublicFundsWapBef() {
        return publicFundsWapBef;
    }

    @JsonSetter("publicFundsWapBef")
    public void setPublicFundsWapBef(String publicFundsWapBef) {
        this.publicFundsWapBef = publicFundsWapBef;
    }

    @JsonGetter("IPO_OVERALL_STATUS")
    public String getIpoStatus() {
        return ipoStatus;
    }

    @JsonSetter("ipoStatus")
    public void setIpoStatus(String ipoStatus) {
        this.ipoStatus = ipoStatus;
    }

    @JsonGetter("announcementUrl")
    public String getAnnouncementUrl() {
        return announcementUrl;
    }

    @JsonSetter("announcementUrl")
    public void setAnnouncementUrl(String announcementUrl) {
        this.announcementUrl = announcementUrl;
    }

    @JsonGetter("STOCK_TYPE")
    public String getStockType() {
        return stockType;
    }

    @JsonSetter("stockType")
    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    @JsonGetter("offlineInvestorString")
    public String getOfflineInvestorString() {
        return offlineInvestorString;
    }

    @JsonSetter("offlineInvestorString")
    public void setOfflineInvestorString(String offlineInvestorString) {
        this.offlineInvestorString = offlineInvestorString;
    }

    @JsonGetter("SECURITY_NAME")
    public String getStockAbbrName() {
        return stockAbbrName;
    }

    @JsonSetter("stockAbbrName")
    public void setStockAbbrName(String stockAbbrName) {
        this.stockAbbrName = stockAbbrName;
    }

    @JsonGetter("lotWinningRate")
    public String getLotWinningRate() {
        return lotWinningRate;
    }

    @JsonSetter("lotWinningRate")
    public void setLotWinningRate(String lotWinningRate) {
        this.lotWinningRate = lotWinningRate;
    }

    @JsonGetter("WEIGHTED_AVG_PRICE_BEF")
    public String getWeightedAvgPriceBef() {
        return weightedAvgPriceBef;
    }

    @JsonSetter("weightedAvgPriceBef")
    public void setWeightedAvgPriceBef(String weightedAvgPriceBef) {
        this.weightedAvgPriceBef = weightedAvgPriceBef;
    }

    @JsonGetter("onlineCirculation")
    public String getOnlineCirculation() {
        return onlineCirculation;
    }

    @JsonSetter("onlineCirculation")
    public void setOnlineCirculation(String onlineCirculation) {
        this.onlineCirculation = onlineCirculation;
    }

    @JsonGetter("SECURITY_CODE")
    public String getStockCode() {
        return stockCode;
    }

    @JsonSetter("stockCode")
    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    @JsonGetter("announceSuccRateRsDate")
    public String getAnnounceSuccRateRsDate() {
        return announceSuccRateRsDate;
    }

    @JsonSetter("announceSuccRateRsDate")
    public void setAnnounceSuccRateRsDate(String announceSuccRateRsDate) {
        this.announceSuccRateRsDate = announceSuccRateRsDate;
    }

    @JsonGetter("ISSUANCE_PRICE_EARNINGS_RATIO")
    public String getIssuancePriceEarningsRatio() {
        return issuancePriceEarningsRatio;
    }

    @JsonSetter("issuancePriceEarningsRatio")
    public void setIssuancePriceEarningsRatio(String issuancePriceEarningsRatio) {
        this.issuancePriceEarningsRatio = issuancePriceEarningsRatio;
    }

    @JsonGetter("MEDIAN_PRICE_BEFORE")
    public String getMedianPriceBefore() {
        return medianPriceBefore;
    }

    @JsonSetter("medianPriceBefore")
    public void setMedianPriceBefore(String medianPriceBefore) {
        this.medianPriceBefore = medianPriceBefore;
    }

    @JsonGetter("PUBLIC_FUNDS_MEDIAN_PRICE_AFT")
    public String getPublicFundsMedianPriceAft() {
        return publicFundsMedianPriceAft;
    }

    @JsonSetter("publicFundsMedianPriceAft")
    public void setPublicFundsMedianPriceAft(String publicFundsMedianPriceAft) {
        this.publicFundsMedianPriceAft = publicFundsMedianPriceAft;
    }

    @JsonGetter("PUBLIC_FUNDS_WAP_AFT")
    public String getPublicFundsWapAft() {
        return publicFundsWapAft;
    }

    @JsonSetter("publicFundsWapAft")
    public void setPublicFundsWapAft(String publicFundsWapAft) {
        this.publicFundsWapAft = publicFundsWapAft;
    }

    @JsonGetter("MEDIAN_PRICE_AFTER")
    public String getMedianPriceAfter() {
        return medianPriceAfter;
    }

    @JsonSetter("medianPriceAfter")
    public void setMedianPriceAfter(String medianPriceAfter) {
        this.medianPriceAfter = medianPriceAfter;
    }

    @JsonGetter("SECURITY_EXPAND_NAME")
    public String getSecurityExpandName() {
        return securityExpandName;
    }

    @JsonSetter("securityExpandName")
    public void setSecurityExpandName(String securityExpandName) {
        this.securityExpandName = securityExpandName;
    }

    @JsonGetter("offlineCirculation")
    public String getOfflineCirculation() {
        return offlineCirculation;
    }

    @JsonSetter("offlineCirculation")
    public void setOfflineCirculation(String offlineCirculation) {
        this.offlineCirculation = offlineCirculation;
    }

    @JsonGetter("totalInitialIssue")
    public String getTotalInitialIssue() {
        return totalInitialIssue;
    }

    @JsonSetter("totalInitialIssue")
    public void setTotalInitialIssue(String totalInitialIssue) {
        this.totalInitialIssue = totalInitialIssue;
    }

    @JsonGetter("OFFLINE_ISSUANCE_END_DATE")
    public String getOfflineIssuanceEndDate() {
        return offlineIssuanceEndDate;
    }

    @JsonSetter("offlineIssuanceEndDate")
    public void setOfflineIssuanceEndDate(String offlineIssuanceEndDate) {
        this.offlineIssuanceEndDate = offlineIssuanceEndDate;
    }

    @JsonGetter("ALLOTMENT_SHARES")
    public String getAllotmentShares() {
        return allotmentShares;
    }

    @JsonSetter("allotmentShares")
    public void setAllotmentShares(String allotmentShares) {
        this.allotmentShares = allotmentShares;
    }

    @JsonGetter("PAYMENT_START_DATE")
    public String getPaymentStartDate() {
        return paymentStartDate;
    }

    @JsonSetter("paymentStartDate")
    public void setPaymentStartDate(String paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
    }

    @JsonGetter("WEIGHTED_AVG_PRICE_AFT")
    public String getWeightedAvgPriceAft() {
        return weightedAvgPriceAft;
    }

    @JsonSetter("weightedAvgPriceAft")
    public void setWeightedAvgPriceAft(String weightedAvgPriceAft) {
        this.weightedAvgPriceAft = weightedAvgPriceAft;
    }
}
