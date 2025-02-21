package com.sse.sseapp.proxy.soa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/4/13 15:31 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class QuatAbelDto {
    private List<QuatAbelBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @Data
    public static class QuatAbelBean extends RespContentVO {
        /**
         * accuTradeDays : 0
         * changePrice : 0.03
         * changeRate : 0.41783
         * closePrice : 7.21
         * dateType : D
         * exchangeRate : 0.07
         * highAmt : 14037.36
         * highAmtDate : 20230412
         * highPrice : 7.23
         * highPriceDate : 20230412
         * highVol : 1947.57
         * highVolDate : 20230412
         * lowAmt : 14037.36
         * lowAmtDate : 20230412
         * lowPrice : 7.17
         * lowPriceDate : 20230412
         * lowVol : 1947.57
         * lowVolDate : 20230412
         * marketValue : 21162918.0
         * negotiableValue : 21162918.0
         * openPrice : 7.2
         * peRate : 3.99278
         * productId : 600000
         * productName : 浦发银行
         * swingRate : 0.83682
         * totalTradingAmount : 14037.36
         * totalTradingVol : 1947.57
         * txDate : 2023-04-12
         */
        @JsonProperty("accuTradeDays")
        private String accuTradeDays;
        @JsonProperty("changePrice")
        private String changePrice;
        @JsonProperty("changeRate")
        private String changeRate;
        @JsonProperty("closePrice")
        private String closePrice;
        @JsonProperty("dateType")
        private String dateType;
        @JsonProperty("exchangeRate")
        private String exchangeRate;
        @JsonProperty("highAmt")
        private String highAmt;
        @JsonProperty("highAmtDate")
        private String highAmtDate;
        @JsonProperty("highPrice")
        private String highPrice;
        @JsonProperty("highPriceDate")
        private String highPriceDate;
        @JsonProperty("highVol")
        private String highVol;
        @JsonProperty("highVolDate")
        private String highVolDate;
        @JsonProperty("lowAmt")
        private String lowAmt;
        @JsonProperty("lowAmtDate")
        private String lowAmtDate;
        @JsonProperty("lowPrice")
        private String lowPrice;
        @JsonProperty("lowPriceDate")
        private String lowPriceDate;
        @JsonProperty("lowVol")
        private String lowVol;
        @JsonProperty("lowVolDate")
        private String lowVolDate;
        @JsonProperty("marketValue")
        private String marketValue;
        @JsonProperty("negotiableValue")
        private String negotiableValue;
        @JsonProperty("openPrice")
        private String openPrice;
        @JsonProperty("peRate")
        private String peRate;
        @JsonProperty("productId")
        private String productId;
        @JsonProperty("productName")
        private String productName;
        @JsonProperty("swingRate")
        private String swingRate;
        @JsonProperty("totalTradingAmount")
        private String totalTradingAmount;
        @JsonProperty("totalTradingVol")
        private String totalTradingVol;
        @JsonProperty("txDate")
        private String txDate;
    }
}
