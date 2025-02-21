package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 债券做市-基准做市品种-做市品种调整信息输出对象
 *
 * @author wy
 * @date 2023-08-07
 */
@Data
public class MarketMakingInfoResBody {

    /**
     * 债券代码
     */
    @JsonProperty("BOND_CODE")
    private String bondCode;

    /**
     * 债券简称
     */
    @JsonProperty("BOND_ABBR")
    private String bondAbbr;

    /**
     * 扩位简称
     */
    @JsonProperty("EXPAND_ABBR")
    private String expandAbbr;

    /**
     * 债券类型
     * DBT0000:信用债
     * INT0000:利率债
     */
    @JsonProperty("BOND_TYPE")
    private String bondType;

    /**
     * 调整方向
     * TR:调入
     * TC:调出
     */
    @JsonProperty("MOD_DIRECT")
    private String modDirect;

    /**
     * 参考生效日期，格式为yyyyMMdd
     */
    @JsonProperty("VALID_DATE")
    private String validDate;

}
