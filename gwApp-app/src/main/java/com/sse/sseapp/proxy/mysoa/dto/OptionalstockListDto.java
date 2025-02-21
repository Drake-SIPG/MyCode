package com.sse.sseapp.proxy.mysoa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/4/3 14:26 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OptionalstockListDto {
    @JsonProperty("followCompanyCount")
    private Integer followCompanyCount;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("status")
    private String status;
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("followCompanies")
    private List<FollowCompaniesDTO> followCompanies;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class FollowCompaniesDTO {
        @JsonProperty("addedTime")
        private String addedTime;
        @JsonProperty("productSubtype")
        private String productSubtype;
        @JsonProperty("stockCode")
        private String stockCode;
        @JsonProperty("stockName")
        private String stockName;
        @JsonProperty("stockType")
        private String stockType;
        @JsonProperty("tradeMarket")
        private String tradeMarket;
    }
}
