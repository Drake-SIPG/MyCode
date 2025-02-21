package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;


/**
 * 债券成交概览
 *
 * @author wy
 * @date 2023-08-10
 */
@Data
public class TurnoverReqBody extends ReqContentVO {
    @JsonProperty("tradeDate")
    private String TRADE_DATE;

    private String sqlId = "COMMON_SSEBOND_SCSJ_SCTJ_SCGL_ZQCJGL_CX_L";
}
