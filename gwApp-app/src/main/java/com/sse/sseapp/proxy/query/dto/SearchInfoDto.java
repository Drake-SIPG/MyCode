package com.sse.sseapp.proxy.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/4/4 14:27 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SearchInfoDto {
    @JsonProperty("channelCode")
    private String channelCode;
    @JsonProperty("code")
    private String code;
    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("docType")
    private String docType;
    @JsonProperty("keyword")
    private String keyword;
    @JsonProperty("keywordPosition")
    private String keywordPosition;
    @JsonProperty("limit")
    private Integer limit;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("orderByDirection")
    private String orderByDirection;
    @JsonProperty("orderByKey")
    private String orderByKey;
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("publishTimeEnd")
    private String publishTimeEnd;
    @JsonProperty("publishTimeStart")
    private String publishTimeStart;
    @JsonProperty("spaceId")
    private Integer spaceId;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class DataDTO {
        @JsonProperty("limit")
        private Integer limit;
        @JsonProperty("page")
        private Integer page;
        @JsonProperty("totalSize")
        private Integer totalSize;
        @JsonProperty("totalPage")
        private Integer totalPage;
        @JsonProperty("costTime")
        private Integer costTime;
        @JsonProperty("correctionKeyword")
        private String correctionKeyword;
        @JsonProperty("originKeyword")
        private String originKeyword;
        @JsonProperty("knowledgeList")
        private List<KnowledgeListDTO> knowledgeList;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Data
        public static class KnowledgeListDTO {
            @JsonProperty("id")
            private Integer id;
            @JsonProperty("imageUrl")
            private String imageUrl;
            @JsonProperty("documentId")
            private String documentId;
            @JsonProperty("url")
            private String url;
            @JsonProperty("title")
            private String title;
            @JsonProperty("rtfContent")
            private String rtfContent;
            @JsonProperty("createTime")
            private String createTime;
            @JsonProperty("updateTime")
            private String updateTime;
            @JsonProperty("updateUserId")
            private String updateUserId;
            @JsonProperty("updateUserName")
            private String updateUserName;
            @JsonProperty("authorId")
            private String authorId;
            @JsonProperty("author")
            private String author;
            @JsonProperty("pageviews")
            private Integer pageviews;
            @JsonProperty("textSummarization")
            private String textSummarization;
            @JsonProperty("paperDocType")
            private String paperDocType;
            @JsonProperty("documentType")
            private String documentType;
            @JsonProperty("tagList")
            private String tagList;
            @JsonProperty("shareCount")
            private String shareCount;
            @JsonProperty("likeCount")
            private String likeCount;
            @JsonProperty("disLikeCount")
            private String disLikeCount;
            @JsonProperty("collectionCount")
            private String collectionCount;
            @JsonProperty("spaceId")
            private Integer spaceId;
            @JsonProperty("spaceName")
            private String spaceName;
            @JsonProperty("newFlag")
            private Boolean newFlag;
            @JsonProperty("score")
            private Double score;
            @JsonProperty("rule")
            private Boolean rule;
            @JsonProperty("folderFullPath")
            private List<String> folderFullPath;
            @JsonProperty("channelIdList")
            private List<Integer> channelIdList;
            @JsonProperty("extend")
            private List<ExtendDTO> extend;

            @JsonIgnoreProperties(ignoreUnknown = true)
            @Data
            public static class ExtendDTO {
                @JsonProperty("name")
                private String name;
                @JsonProperty("value")
                private String value;
            }
        }
    }
}
