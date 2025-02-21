package com.sse.sseapp.proxy.soa.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author mateng
 * @since 2023/7/13 15:47
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class CirculationMarketValueDto extends RespContentVO {


    /**
     * totalmarketvalue : 3.998447202053683E13
     * tradeDate : 20230712
     * marginMktvalueRatio : 2.11
     * marginbalance : 841925221144
     */

    private String mkTotalValue;
    private String tradeDate;
    private String marginMktvalueRatio;
    private String marginBalance;

    @JsonGetter("totalmarketvalue")
    public String getMkTotalValue() {
        return mkTotalValue;
    }

    @JsonSetter("mkTotalValue")
    public void setMkTotalValue(String mkTotalValue) {
        this.mkTotalValue = mkTotalValue;
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
