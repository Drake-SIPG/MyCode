package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class VoiceReqBody extends ReqContentVO {

    /**
     * App包名
     */
    private String appBundle;

    /**
     * 版本
     */
    private String appVersion;

    /**
     * 声音类型：0：一个值，1：多个值
     */
    private String voiceType;
}
