package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class DailyStatisticsReqBody extends ReqContentVO {
    private String tradeDate;
    private String sqlId = "COMMON_SSE_ZQPZ_YSP_QQ_SJTJ_MRTJ_CX";
}
