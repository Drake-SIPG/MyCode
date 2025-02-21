package com.sse.sseapp.proxy.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/4/4 14:27 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SearchStockDto {
    @JsonProperty("jsonCallBack")
    private String jsonCallBack;
    @JsonProperty("orderby")
    private Object orderby;
    @JsonProperty("search")
    private String search;
    @JsonProperty("searchword")
    private String searchword;
    @JsonProperty("result")
    private List<DataDTO> result;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class DataDTO {
        @JsonProperty("SUB_TYPE")
        private String SUB_TYPE;
        @JsonProperty("SEC_CODE")
        private String SEC_CODE;
        @JsonProperty("SEC_NAME_FULL")
        private String SEC_NAME_FULL;
        @JsonProperty("SEC_TYPE")
        private String SEC_TYPE;
    }
}
