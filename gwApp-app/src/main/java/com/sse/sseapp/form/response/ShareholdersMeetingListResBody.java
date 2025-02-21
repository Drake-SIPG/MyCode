package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ShareholdersMeetingListResBody extends RespContentVO {
    /**
     *股东大会召开日
     */
    @JsonProperty("CONVENE_DATE")
    private String conveneDate;
    /**
     *权益登记日
     */
    @JsonProperty("EQUITY_DATE")
    private String equityDate;
    /**
     *股东大会涉及证券代码
     */
    @JsonProperty("SEC_CODE")
    private String secCode;
    /**
     *中文证券名称
     */
    @JsonProperty("SEC_NAME_CN")
    private String secNameCn;
    /**
     *序号
     */
    @JsonProperty("NUM")
    private String num;
    /**
     *备注
     */
    @JsonProperty("SPARE")
    private String spare;
}
