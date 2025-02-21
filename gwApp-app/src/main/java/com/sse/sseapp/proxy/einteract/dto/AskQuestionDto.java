package com.sse.sseapp.proxy.einteract.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/13 19:34
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AskQuestionDto {

    /**
     * 提问成功返回的信息
     */
    private String reason;

    private Boolean state;

    private Integer status;

    /**
     * 以下都是返回失败的信息
     */
    private String appRetrunCode;

    private String appRetrunCodeMsg;

    private ExternalInfo externalInfo;

    /**
     * 返回失败会有的包装类
     */
    @Data
    public static class ExternalInfo{
        private String reason;

        private String status;
    }
}
