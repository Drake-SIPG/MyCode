package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetTypeReqBody extends ReqContentVO {
    @JsonProperty("CATEGORY_PARENT_CODE")
    private String CATEGORY_PARENT_CODE;
    private String sqlId = "COMMON_JJZWZ_JJLB_JJLX_C";
}
