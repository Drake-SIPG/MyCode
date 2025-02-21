package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 今日新股输出对象
 *
 * @author wy
 * @date 2023-06-25
 */
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class NewSharesDataResBody extends RespContentVO {

    /**
     * 股票代码
     */
    private String stockCode;

    /**
     * 股票简称
     */
    private String stockAbbrName;

    /**
     * 股票类型 ：0-主板，1-CDR, 2-科创板
     */
    private String stockType;

    /**
     * 发行总量(万股)
     */
    private BigDecimal totalIssued;
    private BigDecimal ipoStatus;
    private String onlineIssuanceDate;
    private BigDecimal issuePrice;
    private BigDecimal issuancePriceEarningsRatio;
    private BigDecimal onlinePurchaseLimit;

    private String productType;
    private String productSubType;

    @JsonGetter("totalIssued")
    public BigDecimal getTotalIssued() {
        return totalIssued;
    }

}
