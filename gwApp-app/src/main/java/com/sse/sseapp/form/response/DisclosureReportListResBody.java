package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 股票数据-上市公司披露报告列表接口
 *
 * @author wy
 * @date 2023-08-10
 */
@Data
public class DisclosureReportListResBody {

    /**
     * 交易代码
     */
    @JsonProperty("STOCK_ID")
    private String stockId;

    /**
     * 报告所属年度
     */
    @JsonProperty("REPORT_YEAR")
    private String reportYear;

    /**
     * 定期报告周期编码
     */
    @JsonProperty("REPORT_PERIOD_ID")
    private String reportPeriodId;

    /**
     * 定期报告周期名称
     */
    @JsonProperty("REPORT_PERIOD_NAME")
    private String reportPeriodName;

    /**
     * 实际发布日期（yyyy-MM-dd）
     */
    @JsonProperty("ACTUAL_DATE")
    private String actualDate;

    /**
     * 公司代码
     */
    @JsonProperty("COMPANY_CODE")
    private String companyCode;

    /**
     * 公告类型：
     * L011=年报
     * L012=半年报
     * L013=第一季报
     * L014=第三季报
     */
    @JsonProperty("BULLETIN_TYPE")
    private String bulletinType;

    /**
     * 公司简称
     */
    @JsonProperty("COMPANY_ABBR")
    private String companyAbbr;

}
