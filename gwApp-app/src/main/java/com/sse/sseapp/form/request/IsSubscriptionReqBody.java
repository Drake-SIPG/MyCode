package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class IsSubscriptionReqBody extends ReqContentVO {
    private String bondCode;
}
