package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ProMiShoResBody extends RespContentVO {
    @JsonProperty("comments")
    private String comments;
    @JsonProperty("companyCode")
    private String companyCode;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("promiseContent")
    private String promiseContent;
    @JsonProperty("promiseDate")
    private String promiseDate;
    @JsonProperty("promiseExpCompletedDate")
    private String promiseExpCompletedDate;
    @JsonProperty("promiseRelCompletedDate")
    private String promiseRelCompletedDate;
    @JsonProperty("promiseItemType")
    private String promiseItemType;
    @JsonProperty("promiseNo")
    private String promiseNo;
    @JsonProperty("promisePerformType")
    private String promisePerformType;
    @JsonProperty("promiseStatus")
    private String promiseStatus;
    @JsonProperty("promiseSubjectName")
    private String promiseSubjectName;
    @JsonProperty("promiseSubjectType")
    private String promiseSubjectType;
    @JsonProperty("promiseType")
    private String promiseType;
    @JsonProperty("seq")
    private String seq;
    @JsonProperty("secNameFull")
    private String secNameFull;
}
