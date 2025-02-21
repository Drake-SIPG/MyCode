package com.sse.sseapp.form.response;


import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecommendNoticeResBody extends RespContentVO {

    private String noticeId;
    private String noticeTitle;
    private String noticeType;
    private String noticeDate;
    private String noticeContent;
    private String gpdm;
    private String startTime;
    private String noticeStatus;
    private String noticeFileType;
    private String endDate;
    private String dateFormat;
    private String noticeImageUrl;
    private String noticeDataType;
    private String noticeDetailUrl;
    private String companyName;
    private String endTime;
    private String eduViedoRequestUrl;

}
