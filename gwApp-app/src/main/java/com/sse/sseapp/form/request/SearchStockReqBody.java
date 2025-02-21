package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class SearchStockReqBody extends ReqContentVO {
//    private String search = "ycxjs";

    @JsonProperty("SEC_CODE")
    private String SEC_CODE = "";

    private String sqlId = "COMMON_SSE_ZQLX_C";

    private Boolean isPagination = false;

}
