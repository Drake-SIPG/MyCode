package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetCompNoticeListDtoResBody extends RespContentVO {
    private String bulletinHeading;
    private String bulletinType;
    private String bulletinYear;
    private String securityCode;
    private String sseDate;
    private String title;
    private String url;
    private String fileName;
    private String noticeImageUrl;
    private String securityName;
    private String bulletinTypeDesc;
    private String orgBulletinType;
    private String orgFileType;
    private String bulletinId;
}
