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
public class FavouriteResBody extends RespContentVO {

    @JsonProperty("docId")
    private String docId;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("docType")
    private String docType;
    @JsonProperty("docTitle")
    private String docTitle;
    @JsonProperty("docURL")
    private String docURL;
    @JsonProperty("docPublishTime")
    private String docPublishTime;
    @JsonProperty("addedChannel")
    private Integer addedChannel;
    @JsonProperty("addedTime")
    private String addedTime;
    @JsonProperty("deviceType")
    private Integer deviceType;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("stockCode")
    private String stockCode;
    @JsonProperty("stockName")
    private String stockName;


//    userId	int	用户ID
//    docId	varchar(100)	收藏文档ID
//    docType	varchar(50)	文档类型
//    docTitle	varchar(100)	文档标题
//    docURL	varchar(255)	文档URL
//    docPublishTime	varchar(15)	YYYYMMDD H24MMSS文档发布时间
//    addedChannel	int	添加来源，0：网站，1：APP
//    addedTime	varchar(15)	添加收藏时间
//    deviceType	int	设备类型，3：Android，4：IOS
//    deviceId	varchar(50)	设备ID
//    stockCode	varchar(10)	证券代码
//    stockName	varchar(255)	公司名称

}
