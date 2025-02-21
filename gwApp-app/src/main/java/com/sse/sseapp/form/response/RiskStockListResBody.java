package com.sse.sseapp.form.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RiskStockListResBody extends RespContentVO {

    /**
     * productcode : 600078
     * stocktype : EQU
     * productsubtype : ASH
     * productshortname : ST澄星
     */

    private String productcode;
    private String stocktype;
    private String productsubtype;
    private String productshortname;
    @JsonProperty("INSTRUMENT_ID")
    private String INSTRUMENT_ID;
    @JsonProperty("INSTRUMENT_SHORT")
    private String INSTRUMENT_SHORT;

}
