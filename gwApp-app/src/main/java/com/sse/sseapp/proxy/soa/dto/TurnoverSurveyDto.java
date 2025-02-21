package com.sse.sseapp.proxy.soa.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author mateng
 * @since 2023/7/13 11:33
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class TurnoverSurveyDto extends RespContentVO {


    /**
     * financingAmount : 37212492014
     * financingBalance : 796676029935
     * lendingBalance : 6466951539
     * tradeDate : 20230427
     * lendingBalanceValue : 60041536638
     * lendingSellVolumn : 625056460
     * marginBalance : 856717566573
     */

    private String financingAmount;
    private String financingBalance;
    private String lendingBalance;
    private String tradeDate;
    private String lendingBalanceValue;
    private String lendingSellVolumn;
    private String marginBalance;

    @JsonGetter("buy")
    public String getFinancingAmount() {
        return financingAmount;
    }

    @JsonSetter("financingAmount")
    public void setFinancingAmount(String financingAmount) {
        this.financingAmount = financingAmount;
    }

    @JsonGetter("financingbalance")
    public String getFinancingBalance() {
        return financingBalance;
    }

    @JsonSetter("financingBalance")
    public void setFinancingBalance(String financingBalance) {
        this.financingBalance = financingBalance;
    }

    @JsonGetter("margin")
    public String getLendingBalance() {
        return lendingBalance;
    }

    @JsonSetter("lendingBalance")
    public void setLendingBalance(String lendingBalance) {
        this.lendingBalance = lendingBalance;
    }

    @JsonGetter("tradeDate")
    public String getTradeDate() {
        return tradeDate;
    }

    @JsonSetter("tradeDate")
    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    @JsonGetter("marginamount")
    public String getLendingBalanceValue() {
        return lendingBalanceValue;
    }

    @JsonSetter("lendingBalanceValue")
    public void setLendingBalanceValue(String lendingBalanceValue) {
        this.lendingBalanceValue = lendingBalanceValue;
    }

    @JsonGetter("lendingSellVolumn")
    public String getLendingSellVolumn() {
        return lendingSellVolumn;
    }

    @JsonSetter("lendingSellVolumn")
    public void setLendingSellVolumn(String lendingSellVolumn) {
        this.lendingSellVolumn = lendingSellVolumn;
    }

    @JsonGetter("marginbalance")
    public String getMarginBalance() {
        return marginBalance;
    }

    @JsonSetter("marginBalance")
    public void setMarginBalance(String marginBalance) {
        this.marginBalance = marginBalance;
    }
}
