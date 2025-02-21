package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
@Data
public class StockOptionResBody extends RespContentVO {


    @JsonProperty("actionErrors")
    private List<?> actionErrors;
    @JsonProperty("actionMessages")
    private List<?> actionMessages;
    @JsonProperty("fieldErrors")
    private FieldErrorsDTO fieldErrors;
    @JsonProperty("isPagination")
    private String isPagination;
    @JsonProperty("jsonCallBack")
    private String jsonCallBack;
    @JsonProperty("locale")
    private String locale;
    @JsonProperty("pageHelp")
    private PageHelpDTO pageHelp;
    @JsonProperty("pageNo")
    private String pageNo;
    @JsonProperty("pageSize")
    private String pageSize;
    @JsonProperty("queryDate")
    private String queryDate;
    @JsonProperty("result")
    private List<ResultDTO> result;
    @JsonProperty("securityCode")
    private String securityCode;
    @JsonProperty("sqlId")
    private String sqlId;
    @JsonProperty("texts")
    private String texts;
    @JsonProperty("type")
    private String type;
    @JsonProperty("validateCode")
    private String validateCode;

    @NoArgsConstructor
    @Data
    public static class FieldErrorsDTO {
    }

    @NoArgsConstructor
    @Data
    public static class PageHelpDTO {
        @JsonProperty("beginPage")
        private Integer beginPage;
        @JsonProperty("cacheSize")
        private Integer cacheSize;
        @JsonProperty("data")
        private List<DataDTO> data;
        @JsonProperty("endDate")
        private String endDate;
        @JsonProperty("endPage")
        private Integer endPage;
        @JsonProperty("objectResult")
        private String objectResult;
        @JsonProperty("pageCount")
        private Integer pageCount;
        @JsonProperty("pageNo")
        private Integer pageNo;
        @JsonProperty("pageSize")
        private Integer pageSize;
        @JsonProperty("searchDate")
        private String searchDate;
        @JsonProperty("sort")
        private String sort;
        @JsonProperty("startDate")
        private String startDate;
        @JsonProperty("total")
        private Integer total;

        @NoArgsConstructor
        @Data
        public static class DataDTO {
            @JsonProperty("CALL_VOLUME")
            private String callVolume;
            @JsonProperty("LEAVES_QTY")
            private String leavesQty;
            @JsonProperty("CP_RATE")
            private String cpRate;
            @JsonProperty("PUT_VOLUME")
            private String putVolume;
            @JsonProperty("NUM")
            private String num;
            @JsonProperty("TRADE_DATE")
            private String tradeDate;
            @JsonProperty("TOTAL_VOLUME")
            private String totalVolume;
            @JsonProperty("SECURITY_CODE")
            private String securityCode;
            @JsonProperty("LEAVES_CALL_QTY")
            private String leavesCallQty;
            @JsonProperty("LEAVES_PUT_QTY")
            private String leavesPutQty;
            @JsonProperty("SECURITY_ABBR")
            private String securityAbbr;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ResultDTO {
        @JsonProperty("CALL_VOLUME")
        private String callVolume;
        @JsonProperty("LEAVES_QTY")
        private String leavesQty;
        @JsonProperty("CP_RATE")
        private String cpRate;
        @JsonProperty("PUT_VOLUME")
        private String putVolume;
        @JsonProperty("NUM")
        private String num;
        @JsonProperty("TRADE_DATE")
        private String tradeDate;
        @JsonProperty("TOTAL_VOLUME")
        private String totalVolume;
        @JsonProperty("SECURITY_CODE")
        private String securityCode;
        @JsonProperty("LEAVES_CALL_QTY")
        private String leavesCallQty;
        @JsonProperty("LEAVES_PUT_QTY")
        private String leavesPutQty;
        @JsonProperty("SECURITY_ABBR")
        private String securityAbbr;
    }
}
