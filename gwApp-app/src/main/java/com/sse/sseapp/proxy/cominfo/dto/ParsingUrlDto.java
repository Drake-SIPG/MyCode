package com.sse.sseapp.proxy.cominfo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/26 10:50
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ParsingUrlDto extends RespContentVO {

    @JsonProperty("docId")
    private String docId;

    @JsonProperty("releaseDate")
    private String releaseDate;

    @JsonProperty("url")
    private String url;

    @JsonProperty("publishdate")
    private String publishdate;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;


}
