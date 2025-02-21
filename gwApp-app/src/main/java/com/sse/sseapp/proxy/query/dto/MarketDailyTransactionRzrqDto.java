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
public class MarketDailyTransactionRzrqDto {
    @JsonProperty("result")
    private List<ResultDTO> result;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class ResultDTO {
        /**
         * rzye : 805311386298
         * rqylje : 63492538974
         * rqyl : 6653688732
         * rzmre : 44363451113
         * rzche : 43960611127
         * rzrqjyzl : 868803925272
         * opDate : 20230419
         * rqmcl : 639965435
         */
        @JsonProperty("rzye")
        private String rzye;
        @JsonProperty("rqylje")
        private String rqylje;
        @JsonProperty("rqyl")
        private String rqyl;
        @JsonProperty("rzmre")
        private String rzmre;
        @JsonProperty("rzche")
        private String rzche;
        @JsonProperty("rzrqjyzl")
        private String rzrqjyzl;
        @JsonProperty("opDate")
        private String opDate;
        @JsonProperty("rqmcl")
        private String rqmcl;
    }
}
