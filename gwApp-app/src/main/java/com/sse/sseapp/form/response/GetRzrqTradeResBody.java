package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/24 17:13
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetRzrqTradeResBody extends RespContentVO {
    /**
     * 证券代码
     */
    private String scrCode;
    /**
     * 证券简称
     */
    private String scrAbbr;
    /**
     * 融资买入，单位：百万
     */
    private String rzBuy;
    /**
     * 融券卖出，单位：百万
     */
    private String rqSell;
    /**
     * 合计，单位：百万
     */
    private String totAmt;
    /**
     * 统计月份，格式"YYYYMM"
     */
    private String mdate;
}
