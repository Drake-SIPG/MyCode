package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class AdvNewResBody extends RespContentVO {

    /**
     * 广告标题
     */
    @JsonProperty("advertTitle")
    private String advertTitle;

    /**
     * 广告跳转地址
     */
    @JsonProperty("advertTargetUrl")
    private String advertTargetUrl;

    /**
     * 广告显示时长
     */
    @JsonProperty("showTime")
    private String showTime;

    /**
     * 是否显示
     */
    @JsonProperty("needShow")
    private String needShow;

    /**
     * 显示类型：0-每次都显示，1-一天只显示一次
     */
    @JsonProperty("showType")
    private String showType;

    /**
     * png,gif,jpg
     */
    @JsonProperty("imgType")
    private String imgType;

    /**
     * 图片地址
     */
    @JsonProperty("imgUrl")
    private String imgUrl;

    /**
     * wifi下载标记  0：小于阈值；1大于阈值
     */
    @JsonProperty("wifiDown")
    private String wifiDown;

}
