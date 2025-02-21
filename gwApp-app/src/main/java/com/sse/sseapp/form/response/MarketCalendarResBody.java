package com.sse.sseapp.form.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MarketCalendarResBody extends RespContentVO {
    @JsonProperty("stockCode")
    private String stockCode;
    @JsonProperty("stockAbbr")
    private String stockAbbr;
    @JsonProperty("tradeBeginDate")
    private String tradeBeginDate;
    @JsonProperty("tradeEndDate")
    private String tradeEndDate;
    @JsonProperty("title")
    private String title;
    @JsonProperty("type")
    private String type;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("bizSeq")
    private String bizSeq;
    @JsonProperty("bizType")
    private String bizType;
    @JsonProperty("bizTypeDesc")
    private String bizTypeDesc;
    @JsonProperty("subTypeDesc")
    private String subTypeDesc;
    @JsonProperty("subtype")
    private String subtype;
    @JsonProperty("content")
    private String content;
    @JsonProperty("updateTime")
    private String updateTime;
    @JsonProperty("ext1")
    private String ext1;
    @JsonProperty("ext2")
    private String ext2;
    @JsonProperty("ext3")
    private String ext3;
    private String rsurl;


//    <stockCode clientmapping="code"></stockCode>
//				<stockAbbr clientmapping="name"></stockAbbr>
//				<tradeBeginDate></tradeBeginDate>
//				<tradeEndDate></tradeEndDate>
//				<title></title>
//				<summary></summary>
//				<bizType></bizType>
//				<bizTypeDesc></bizTypeDesc>
//				<subTypeDesc></subTypeDesc>
//				<content></content>
//				<updateTime></updateTime>
//				<ext1></ext1>
//				<ext2></ext2>
//				<ext3></ext3>
}
