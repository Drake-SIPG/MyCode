package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 首页菜单数据
 *
 * @author wy
 * @date 2023-06-29
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class NavHomeListResBody extends RespContentVO {
    @JsonProperty("needUpdate")
    private Integer needUpdate;
    @JsonProperty("timeStamp")
    private String timeStamp;
    @JsonProperty("list")
    private List<ListVo> list;
    @JsonProperty("homeList")
    private List<HomeListVo> homeList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class ListVo {
        @JsonProperty("num")
        private Integer num;
        @JsonProperty("parentNum")
        private Integer parentNum;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class HomeListVo {
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
        @JsonProperty("parentTag")
        private String parentTag;
        @JsonProperty("selectedImageURLRed")
        private String selectedImageURLRed;
        @JsonProperty("webURL")
        private String webURL;
        @JsonProperty("columnPageType")
        private String columnPageType;
    }
}
