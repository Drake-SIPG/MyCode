package com.sse.sseapp.domain.push;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 债券回售实体
 *
 * @author wy
 * @date 2023-11-14
 */
@Data
public class AppBondPull {

    @JsonProperty("pageHelp")
    private PageHelpDTO pageHelp;
    @JsonProperty("result")
    private List<ResultDTO> result;

    @NoArgsConstructor
    @Data
    public static class PageHelpDTO {
        @JsonProperty("pageCount")
        private Integer pageCount;
    }

    @NoArgsConstructor
    @Data
    public static class ResultDTO {
        @JsonProperty("FACE_RATE")
        private String FACE_RATE;
        @JsonProperty("BOND_CODE")
        private String BOND_CODE;
        @JsonProperty("ACTION")
        private String ACTION;
        @JsonProperty("ISSUE_VALUE")
        private String ISSUE_VALUE;
        @JsonProperty("BOND_TYPE")
        private String BOND_TYPE;
        @JsonProperty("BOND_ABBR")
        private String BOND_ABBR;
        @JsonProperty("NUM")
        private String NUM;
        @JsonProperty("START_DATE")
        private String START_DATE;
        @JsonProperty("BOND_RATING")
        private String BOND_RATING;
    }
}
