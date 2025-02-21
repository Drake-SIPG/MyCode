package com.sse.sseapp.form.response;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class IPOListResBody extends RespContentVO {

    /**
     * summary : 重要公告
     * bizType : 1
     * tradeEndDate : 29990101
     * subTypeDesc : 连续停牌
     * updateTime : null
     * stockAbbr : 乾景园林
     * title : 自2022-11-08起连续停牌
     * stockCode : 603778
     * content : null
     * controlType : TR
     * bizTypeDesc : 停复牌
     * bizSeq : 104929
     * tradeBeginDate : 20221108
     * ext3 : null
     * ext2 : null
     * ext1 : 20
     */

    private String summary;
    private String bizType;
    private String tradeEndDate;
    private String subTypeDesc;
    private String updateTime;
    private String stockAbbr;
    private String title;
    private String stockCode;
    private String content;
    private String controlType;
    private String bizTypeDesc;
    private String bizSeq;
    private String tradeBeginDate;
    private String ext3;
    private String ext2;
    private String ext1;
    private String rsurl;
    private String type;
    private String subtype;

    @JsonGetter("name")
    public String getStockAbbr() {
        return stockAbbr;
    }

    @JsonSetter("stockAbbr")
    public void setStockAbbr(String stockAbbr) {
        this.stockAbbr = stockAbbr;
    }

    @JsonGetter("code")
    public String getStockCode() {
        return stockCode;
    }

    @JsonSetter("stockCode")
    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }
}
