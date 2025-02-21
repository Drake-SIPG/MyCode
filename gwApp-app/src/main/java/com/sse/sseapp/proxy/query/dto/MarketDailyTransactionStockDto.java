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
public class MarketDailyTransactionStockDto {
    @JsonProperty("result")
    private List<ResultDTO> result;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class ResultDTO {
        /**
         * TOTAL_VALUE : 448084.7
         * TRADE_VOL : 329.46
         * AVG_PE_RATE : 12.91
         * TOTAL_TO_RATE : 0.8451
         * TRADE_AMT : 3786.54
         * TRADE_NUM : 0.1684
         * NEGO_VALUE : 411278.81
         * NEGO_TO_RATE : 0.9207
         * TRADE_DATE : 20230418
         * LIST_NUM : 1684
         * PRODUCT_CODE : 01
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
