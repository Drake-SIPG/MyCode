package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class NavResBody extends RespContentVO {
    @JsonProperty("needUpdate")
    private Integer needUpdate;
    @JsonProperty("timeStamp")
    private String timeStamp;
    @JsonProperty("list")
    private List<ListDTO> list;
    @JsonProperty("homeList")
    private List<ListDTO> homeList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class ListDTO {
        @JsonProperty("homeNavOrder")
        private Integer homeNavOrder;
        @JsonProperty("navName")
        private String navName;
        @JsonProperty("navTag")
        private String navTag;
        @JsonProperty("normalImageURLRed")
        private String normalImageURLRed;
        @JsonProperty("num")
        private Integer num;
        @JsonProperty("parentNum")
        private Integer parentNum;
        @JsonProperty("selectedImageURLRed")
        private String selectedImageURLRed;
        @JsonProperty("webURL")
        private String webURL;
        @JsonProperty("parentTag")
        private String parentTag;
        @JsonProperty("appBundle")
        private String appBundle;
        @JsonProperty("appVersion")
        private String appVersion;
        @JsonProperty("columnPageType")
        private String columnPageType;
        @JsonProperty("createTime")
        private String createTime;
        @JsonProperty("h5Title")
        private String h5Title;
        @JsonProperty("id")
        private String id;
        @JsonProperty("listURL")
        private String listURL;
        @JsonProperty("navOrder")
        private String navOrder;
        @JsonProperty("normalImageURL")
        private String normalImageURL;
        @JsonProperty("selectedImageURL")
        private String selectedImageURL;
        @JsonProperty("updateTime")
        private String updateTime;
        @JsonProperty("columnSubColumns")
        private List<Object> columnSubColumns;
    }
}
