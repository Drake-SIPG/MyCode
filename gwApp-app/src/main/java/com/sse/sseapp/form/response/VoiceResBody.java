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
public class VoiceResBody extends RespContentVO {

    /**
     * Id编号
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * App包名
     */
    @JsonProperty("appBundle")
    private String appBundle;

    /**
     * 声音id
     */
    @JsonProperty("voiceId")
    private String voiceId;

    /**
     * 声音
     */
    @JsonProperty("voice")
    private String voice;

    /**
     * 最新声音值
     */
    @JsonProperty("voiceNew")
    private String voiceNew;

    /**
     * 版本
     */
    @JsonProperty("appVersion")
    private String appVersion;

    /**
     * 更新时间
     */
    @JsonProperty("updateTime")
    private String updateTime;

    /**
     * 创建时间
     */
    @JsonProperty("createTime")
    private String createTime;

    /**
     * 声音类型：0：一个值，1：多个值
     */
    @JsonProperty("voiceType")
    private String voiceType;

    /**
     * 声音选项(当voiceType=1时该值有效)：0男声，1女声，2默认声
     */
    @JsonProperty("voiceOption")
    private String voiceOption;


}
