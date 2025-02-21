package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ValueRankingReqBody extends ReqContentVO {
    @JsonProperty("SEARCH_DATE")
    private String SEARCH_DATE;
    @JsonProperty("LIST_BOARD")
    private String LIST_BOARD;
    @JsonProperty("TOTAL_VALUE_DESC")
    private String TOTAL_VALUE_DESC;
    @JsonProperty("NEGO_VALUE_DESC")
    private String NEGO_VALUE_DESC;
    private String pageSize;
    private Boolean isPagination = true;
    private String sqlId = "COMMON_SSE_SJ_SZPM_L";
}
