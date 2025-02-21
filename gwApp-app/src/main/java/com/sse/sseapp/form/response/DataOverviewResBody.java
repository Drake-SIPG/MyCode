package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 股票数据-股票数据总貌统计数据
 *
 * @author wy
 * @date 2023-08-10
 */
@Data
public class DataOverviewResBody {

    /**
     * 上市公司数
     */
    @JsonProperty("LIST_COM_NUM")
    private String listComNum;

    /**
     * 上市股票|只
     */
    @JsonProperty("SECURITY_NUM")
    private String securityNum;

    /**
     * 总股本|亿股
     */
    @JsonProperty("TOTAL_ISSUE_VOL")
    private String totalIssueVol;

    /**
     * 流通股本|亿股
     */
    @JsonProperty("NEGO_ISSUE_VOL")
    private String negoIssueVol;

    /**
     * 总市值|亿元
     */
    @JsonProperty("TOTAL_VALUE")
    private String totalValue;

    /**
     * 流通市值|亿元
     */
    @JsonProperty("NEGO_VALUE")
    private String negoValue;

    /**
     * 平均市盈率|倍
     */
    @JsonProperty("AVG_PE_RATIO")
    private String avgPeRatio;

    /**
     * 品种名称：股票、主板A、主板B、科创板
     */
    @JsonProperty("PRODUCT_NAME")
    private String productName;

    /**
     * 交易日期，格式“YYYYMMDD”
     */
    @JsonProperty("TRADE_DATE")
    private String tradeDate;
}
