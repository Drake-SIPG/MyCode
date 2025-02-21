package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * 股票数据-股票数据总貌统计数据
 *
 * @author wy
 * @date 2023-08-10
 */
@Data
public class DataOverviewReqBody extends ReqContentVO {

    @JsonProperty("tradeDate")
    private String TRADE_DATE;

    /**
     * 品种名称：股票、主板、科创板
     */
    @JsonProperty("productName")
    private String PRODUCT_NAME;

    private String sqlId = "COMMON_SSE_SJ_GPSJ_GPSJZM_TJSJ_L";

}
