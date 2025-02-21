package com.sse.sseapp.form.response;

import lombok.Data;

/**
 * 债券做市-基准做市业务情况输出对象
 *
 * @author wy
 * @date 2023-08-08
 */
@Data
public class MarketMakingBusinessInfoResBody {

    /**
     * 序号
     */
    private String rown;

    /**
     * 债券代码
     */
    private String bondCode;

    /**
     * 债券简称
     */
    private String bondAbbr;

    /**
     * 合计成交金额（万元）
     */
    private String totalAmt;

    /**
     * 日期，格式“YYYYMMDD”
     */
    private String tradeDate;

    /**
     * 排名前五做市商名称
     */
    private String firmNames;
}
