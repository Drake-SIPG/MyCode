package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetDueDateReqBody extends ReqContentVO {

    private String sqlId = "COMMON_SSE_WDQQQHYQQDQLBXX_L";
    private String underlying_security_id ;
}