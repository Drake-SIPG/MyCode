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
public class MarketDailyTransactionBondDto {
    @JsonProperty("result")
    private List<ResultDTO> result;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class ResultDTO {
        /**
         * AMOUNT : 1869955.90
         * AVG_PRICE : 101.880
         * VOLUME : 11703
         * TRADE_DATE : 2023-04-18
         * TYPE : 记账式国债
         */
        @JsonProperty("AMOUNT")
        private String amount;
        @JsonProperty("AVG_PRICE")
        private String avgPrice;
        @JsonProperty("VOLUME")
        private String volume;
        @JsonProperty("TRADE_DATE")
        private String tradeDate;
        @JsonProperty("TYPE")
        private String type;
    }
}
