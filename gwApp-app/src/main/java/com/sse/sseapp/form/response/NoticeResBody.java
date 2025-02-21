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
public class NoticeResBody extends RespContentVO {
    @JsonProperty("channelId")
    private String channelId;
    @JsonProperty("cmsDocType")
    private String cmsDocType;
    @JsonProperty("cmsOpDate")
    private String cmsOpDate;
    @JsonProperty("dOCID")
    private String dOCID;
    @JsonProperty("date")
    private String date;
    @JsonProperty("extGSJC")
    private String extGSJC;
    @JsonProperty("extSECURITY_CODE")
    private String extsecurityCode;
    @JsonProperty("extTYPE")
    private String extTYPE;
    @JsonProperty("extTeacher")
    private String extTeacher;
    @JsonProperty("extWTFL")
    private String extWTFL;
    @JsonProperty("parentChannelId")
    private String parentChannelId;
    @JsonProperty("readCount")
    private Integer readCount;
    @JsonProperty("siteId")
    private String siteId;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("stockcode")
    private String stockcode;
    @JsonProperty("title")
    private String title;
    @JsonProperty("type")
    private String type;
    @JsonProperty("url")
    private String url;


//    <createTime clientmapping="date"></createTime>
//				<docId clientmapping="DOCID"></docId>
//				<docType clientmapping="type"></docType>
//				<docURL clientmapping="url"></docURL>
//				<siteId></siteId>
//				<docSize clientmapping="size"></docSize>
//				<docTitle clientmapping="title"></docTitle>
//				<channelId></channelId>
//				<docKeyword></docKeyword>
}
