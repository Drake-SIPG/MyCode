package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StockHoldingChangeResBody {
    @JsonProperty("changeDate")
    private String changeDate;
    @JsonProperty("changeNum")
    private Integer changeNum;
    @JsonProperty("changeReason")
    private String changeReason;
    @JsonProperty("companyAbbr")
    private String companyAbbr;
    @JsonProperty("companyCode")
    private String companyCode;
    @JsonProperty("currencyType")
    private String currencyType;
    @JsonProperty("currentAvgPrice")
    private String currentAvgPrice;
    @JsonProperty("currentNum")
    private Integer currentNum;
    @JsonProperty("duty")
    private String duty;
    @JsonProperty("formDate")
    private String formDate;
    @JsonProperty("holdstockNum")
    private Integer holdstockNum;
    @JsonProperty("name")
    private String name;
    @JsonProperty("stockType")
    private String stockType;
}
