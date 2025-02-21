package com.sse.sseapp.app.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import static java.lang.System.currentTimeMillis;

@SuppressWarnings("all")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ReqBaseVO {
    @JsonProperty("deviceType")
    private String deviceType;
    @JsonProperty("appVersion")
    private String appVersion;
    @JsonProperty("OSType")
    private String OSType;
    @JsonProperty("vendor")
    private String vendor;
    @JsonProperty("ip")
    private String ip;
    @JsonProperty("location")
    private String location;
    @JsonProperty("appBundle")
    private String appBundle;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("timestamp")
    private Long timestamp = currentTimeMillis();
    @JsonProperty("av")
    private String av;
    @JsonProperty("screenSize")
    private String screenSize;
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("appToken")
    private String appToken;
    @JsonProperty("uid")
    private String uid;
}
