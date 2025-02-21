package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class GetNavAllListReqBody extends ReqContentVO {
    @JsonProperty("appBundle")
    private String appBundle;
    @JsonProperty("appVersion")
    private String appVersion;
}
