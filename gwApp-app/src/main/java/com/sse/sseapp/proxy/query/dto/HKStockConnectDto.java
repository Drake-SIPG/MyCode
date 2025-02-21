package com.sse.sseapp.proxy.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/20 10:18
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HKStockConnectDto extends RespContentVO {
    /**
     * 额度信息
     */
    private String quotaInformation;
    /**
     * 剩余额度
     */
    private String residualLimit;

    private referenceRate referenceRate;

    private settlementRate settlementRate;

    @JsonProperty("result")
    private List<resultDto> result;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class resultDto{
        /**
         * 数据日期
         */
        @JsonProperty("TRADE_DATE")
        private String TRADE_DATE;
        /**
         * 当日买入成交金额（亿元）
         */
        @JsonProperty("BUY_AMOUNT")
        private String BUY_AMOUNT;
        /**
         * 当日买入成交笔数（万笔）
         */
        @JsonProperty("BUY_VOLUME")
        private String BUY_VOLUME;
        /**
         * 当日卖出成交金额（亿元）
         */
        @JsonProperty("SELL_AMOUNT")
        private String SELL_AMOUNT;
        /**
         * 当日卖出成交笔数（万笔）
         */
        @JsonProperty("SELL_VOLUME")
        private String SELL_VOLUME;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class referenceRate{
        private String VALID_DATE;
        private String BUY_PRICE;
        private String SELL_PRICE;
        private String CURRENCY_TYPE;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class settlementRate{
        private String VALID_DATE;
        private String BUY_PRICE;
        private String SELL_PRICE;
        private String CURRENCY_TYPE;
    }

}
