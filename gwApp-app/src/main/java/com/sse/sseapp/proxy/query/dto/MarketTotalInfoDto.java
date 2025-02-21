package com.sse.sseapp.proxy.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
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
public class MarketTotalInfoDto {
    @JsonProperty("result")
    private List<ResultDTO> result = Lists.newArrayList();

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class ResultDTO {
        /**
         * TOTAL_VALUE : 518780.74
         * SECURITY_NUM : 2244
         * NEGO_VALUE : 447921.94
         * TRADE_DATE : 20230418
         * TOTAL_TRADE_AMT : 4753.52
         * PRODUCT_NAME : 股票
         */
        @JsonProperty("TOTAL_VALUE")
        private String totalValue;
        @JsonProperty("SECURITY_NUM")
        private String securityNum;
        @JsonProperty("NEGO_VALUE")
        private String negoValue;
        @JsonProperty("TRADE_DATE")
        private String tradeDate;
        @JsonProperty("TOTAL_TRADE_AMT")
        private String totalTradeAmt;
        @JsonProperty("PRODUCT_NAME")
        private String productName;
    }
}
