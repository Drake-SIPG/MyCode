package com.sse.sseapp.proxy.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/4/18 16:43 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MarketDailyTransactionFundDto {
    @JsonProperty("result")
    private List<ResultDTO> result;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class ResultDTO {
        /**
         * TOTAL_VALUE : 13599.65
         * TRADE_VOL : 421.36
         * AVG_PE_RATE : -
         * TOTAL_TO_RATE : 5.7891
         * TRADE_AMT : 787.3
         * TRADE_NUM : 0.0613
         * NEGO_VALUE : 13176.82
         * NEGO_TO_RATE : 5.9749
         * TRADE_DATE : 20230419
         * LIST_NUM : 629
         * PRODUCT_CODE : 05
         */
        @JsonProperty("TOTAL_VALUE")
        private String totalValue;
        @JsonProperty("TRADE_VOL")
        private String tradeVol;
        @JsonProperty("AVG_PE_RATE")
        private String avgPeRate;
        @JsonProperty("TOTAL_TO_RATE")
        private String totalToRate;
        @JsonProperty("TRADE_AMT")
        private String tradeAmt;
        @JsonProperty("TRADE_NUM")
        private String tradeNum;
        @JsonProperty("NEGO_VALUE")
        private String negoValue;
        @JsonProperty("NEGO_TO_RATE")
        private String negoToRate;
        @JsonProperty("TRADE_DATE")
        private String tradeDate;
        @JsonProperty("LIST_NUM")
        private String listNum;
        @JsonProperty("PRODUCT_CODE")
        private String productCode;
    }
}
