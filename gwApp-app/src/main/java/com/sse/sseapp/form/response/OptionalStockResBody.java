package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class OptionalStockResBody extends RespContentVO {

    @JsonProperty("stockCode")
    private String stockCode;
    @JsonProperty("stockName")
    private String stockName;
    @JsonProperty("stockType")
    private String stockType;
    @JsonProperty("productSubtype")
    private String productSubtype;
    @JsonProperty("addedTime")
    private String addedTime;
    @JsonProperty("tradeMarket")
    private String tradeMarket;

//    stockCode varchar(10)	股票代码
//    stockType varchar(6)	产品类型（BON债券、FUN基金、EQU股票、WAR权证、FUT期货、NUL非上市公司）
//    stockName varchar(255)	股票名称
//    productSubtype varchar(6)	详情见产品类型与子类型的描述表
//    tradeMarket varchar(4)	交易市场（SH：沪市、SZ：深市、NA:其他）
//    addedTime	varchar(20)	保存时间，YYYYMMDD HH24:mi:ss

}
