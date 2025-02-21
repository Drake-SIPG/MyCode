package com.sse.sseapp.form.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoadShowDataResBody extends RespContentVO {
    private String noticeType;
    private String noticeTitle;
    private String startTime;
    private String endTime;
    private String noticeDate;
    private String noticeStatus;
    private Integer noticeId;
    private String gpdm;
    private String noticeContent;
    private String noticeImageUrl;
    private String noticeDetailUrl;
    private String companyName;
    private String endDate;
    private String dateFormat;
    private String noticeFileType;
    private String eduViedoRequestUrl;

}
