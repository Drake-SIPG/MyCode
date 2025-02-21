package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * 股票数据-上市公司披露报告列表接口
 *
 * @author wy
 * @date 2023-08-10
 */
@Data
public class DisclosureReportListReqBody extends ReqContentVO {

    /**
     * 公司代码（支持多代码）
     */
    private String stockId;

    /**
     * 是否最年份（是：1，否：空）
     */
    private String isMax;

    /**
     * 年报类型（年报：5000，半年报：1000）
     */
    private String reportPeriodId;

    private String sqlId = "SSE_SHGSXX_PLBGLB_getDiscloseReportList";

}
