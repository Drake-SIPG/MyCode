package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 活跃股排名
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ActivityRankingReqBody extends ReqContentVO {
    @JsonProperty("TRADE_DATE")
    private String TRADE_DATE;
    /**
     * 主板：1 科创板：2
     */
    @JsonProperty("LIST_BOARD")
    private String LIST_BOARD;
    private Boolean isPagination = true;
    private String TRADE_VOL_DESC = "1";
    private String pageSize;
    private String sqlId = "COMMON_SSE_SJ_GPSJ_HYGPM_L";
}
