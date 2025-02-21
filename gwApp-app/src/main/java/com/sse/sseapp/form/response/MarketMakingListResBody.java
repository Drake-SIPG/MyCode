package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 债券做市-基准做市品种-做事品种列表输出对象
 *
 * @author wy
 * @date 2023-08-07
 */
@Data
public class MarketMakingListResBody {

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
     * 文件名日期YYYYMMDD，为当前交易日
     */
    @JsonProperty("FILE_DATE")
    private String fileDate;

}
