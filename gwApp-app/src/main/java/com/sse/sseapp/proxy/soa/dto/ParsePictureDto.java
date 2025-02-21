package com.sse.sseapp.proxy.soa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/15 9:25
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ParsePictureDto extends RespContentVO {

    private String docId;

    private String publishdate;

    private String releaseDate;

    private String title;

    private String url;

    private String content;

    private List<Content> contentObjectList;

    @Data
    public static class Content extends RespContentVO {
        private String img;

        private String text;
    }
}
