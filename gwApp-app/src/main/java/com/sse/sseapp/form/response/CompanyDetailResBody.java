package com.sse.sseapp.form.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class CompanyDetailResBody extends RespContentVO {

    @JsonProperty("reason")
    private String reason;
    @JsonProperty("status")
    private String status;
    @JsonProperty("company")
    private List<CompanyDetailDTO> company;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class CompanyDetailDTO {
        @JsonProperty("pinYin")
        private String pinYin;
        @JsonProperty("countQuestion")
        private Integer countQuestion;
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("shortName")
        private String shortName;
        @JsonProperty("baCount")
        private String baCount;
        @JsonProperty("stockCode")
        private String stockCode;
        @JsonProperty("countAnswer")
        private Integer countAnswer;
        @JsonProperty("countAdded")
        private Integer countAdded;
        @JsonProperty("photoAddress")
        private String photoAddress;
    }
}
