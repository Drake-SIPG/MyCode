package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 当天走势行情数据
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class StockLineReqBody extends ReqContentVO {

    @JsonProperty("code")
    private String code;
    @JsonProperty("stockType")
    private String stockType;
    @JsonProperty("productSubtype")
    private String productSubtype;
    @JsonProperty("lineType")
    private String lineType;
}
