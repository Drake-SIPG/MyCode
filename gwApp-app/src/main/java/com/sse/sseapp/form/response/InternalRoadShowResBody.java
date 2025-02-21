package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class InternalRoadShowResBody extends RespContentVO {


    @JsonProperty("typeName")
    private String typeName;
    @JsonProperty("title")
    private String title;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("endTime")
    private String endTime;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("stockCode")
    private String stockCode;
    @JsonProperty("titleImage")
    private String titleImage;
    @JsonProperty("homeImage")
    private String homeImage;
    @JsonProperty("detailUrl")
    private String detailUrl;
    @JsonProperty("uname")
    private String uname;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("dateFormat")
    private String dateFormat;

}
