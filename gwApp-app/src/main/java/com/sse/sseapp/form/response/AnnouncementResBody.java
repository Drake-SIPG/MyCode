package com.sse.sseapp.form.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class AnnouncementResBody extends RespContentVO {

    private String docId;
    private String docTitle;
    private String channelId;
    private String stockcode;
    private String createTime;
    private String docSize;
    private String docType;
    private String docURL;
    private String cmsOpDate;
    private String extGSJC;
    private String extWTFL;
    private String extGGLX;
    private String extTYPE;
    private String extINTRODUCTION;
    private String extTeacher;
    /**
     * 阅读数
     */
    private Integer readCount;
}
