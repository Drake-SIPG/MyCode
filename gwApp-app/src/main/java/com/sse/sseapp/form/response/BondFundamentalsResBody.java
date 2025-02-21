package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class BondFundamentalsResBody extends RespContentVO {
    /**
     * 票面利率（%）
     */
    @JsonProperty("FACE_RATE")
    private String FACE_RATE;
    /**
     * 债券代码
     */
    @JsonProperty("BOND_CODE")
    private String BOND_CODE;
    /**
     * 主体评级
     */
    @JsonProperty("MAIN_RATING")
    private String MAIN_RATING;
    /**
     * 付息方式
     */
    @JsonProperty("PAY_TYPE")
    private String PAY_TYPE;
    /**
     * 债券类型
     */
    @JsonProperty("BOND_TYPE")
    private String BOND_TYPE;
    /**
     * 债券估值（元）
     */
    @JsonProperty("CLEAN_PRICE")
    private String CLEAN_PRICE;
    /**
     * 标准券折算率
     */
    @JsonProperty("RATE")
    private String RATE;
    /**
     * 债券简称
     */
    @JsonProperty("BOND_ABBR")
    private String BOND_ABBR;

    @JsonProperty("FACE_VALUE")
    private String FACE_VALUE;
    /**
     * 上交所上市总量（亿元）
     */
    @JsonProperty("SSE_TOTAL_AMT")
    private String SSE_TOTAL_AMT;
    /**
     * 到期日期
     */
    @JsonProperty("END_DATE")
    private String END_DATE;

    @JsonProperty("PLEDGE_CODE")
    private String PLEDGE_CODE;

    @JsonProperty("ISSUE_PRICE")
    private String ISSUE_PRICE;
    /**
     * 基准利率（%）
     */
    @JsonProperty("BASE_RATE")
    private String BASE_RATE;
    /**
     * 银行间代码
     */
    @JsonProperty("BANK_CODE")
    private String BANK_CODE;
    /**
     * 债券期限（年）
     */
    @JsonProperty("TERM_YEAR")
    private String TERM_YEAR;
    /**
     * 计息方式
     */
    @JsonProperty("INTEREST_TYPE")
    private String INTEREST_TYPE;
    /**
     * 投资者适当性管理
     */
    @JsonProperty("MANAGE_TYPE")
    private String MANAGE_TYPE;
    /**
     * 债券全称
     */
    @JsonProperty("BOND_FULL")
    private String BOND_FULL;
    /**
     * 起息日期
     */
    @JsonProperty("START_DATE")
    private String START_DATE;
    /**
     * 发行人
     */
    @JsonProperty("ISSUE_OWNER")
    private String ISSUE_OWNER;
    /**
     * 交易方式
     */
    @JsonProperty("TRADE_TYPE")
    private String TRADE_TYPE;

    @JsonProperty("TERM_DAY")
    private String TERM_DAY;
    /**
     * 发行终止日
     */
    @JsonProperty("ONLINE_END_DATE")
    private String ONLINE_END_DATE;
    /**
     * 是否非担保
     */
    @JsonProperty("IS_NOT_DB")
    private String IS_NOT_DB;

    @JsonProperty("ISSUE_VALUE_HM")
    private String ISSUE_VALUE_HM;
    /**
     * 发行量（亿元）
     */
    @JsonProperty("ISSUE_VALUE")
    private String ISSUE_VALUE;

    @JsonProperty("CREDIT_LEVEL")
    private String CREDIT_LEVEL;
    /**
     * 发行起始日
     */
    @JsonProperty("ONLINE_START_DATE")
    private String ONLINE_START_DATE;
    /**
     * 债券评级
     */
    @JsonProperty("BOND_RATING")
    private String BOND_RATING;
    /**
     * 债券扩位简称
     */
    @JsonProperty("SECURITY_ABBR_FULL")
    private String SECURITY_ABBR_FULL;
    /**
     * 上市日期
     */
    @JsonProperty("LISTING_DATE")
    private String LISTING_DATE;
}
