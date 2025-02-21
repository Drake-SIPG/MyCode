package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;


@Data
public class AddFeedbackReqBody extends ReqContentVO {

    @JsonProperty("lastUpdateUser")
    private String lastUpdateUser;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("userName")
    private String userName = "-";
    @JsonProperty("Email")
    private String Email = "-";
    @JsonProperty("contactInfomation")
    private String contactInfomation = "-";
    @JsonProperty("channelId")
    private String channelId;
    @JsonProperty("userQuestion")
    private String userQuestion;
    @JsonProperty("deviceType")
    private String deviceType;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("source")
    private String source = "1";
    @JsonProperty("session")
    private String session;
    @JsonProperty("type")
    private String type;
    @JsonProperty("authCode")
    private String authCode;

}
