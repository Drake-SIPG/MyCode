package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetCompNoticeListReqBody extends ReqContentVO {
    private String pageNo;
    private String pageSize = "20";
    private String title;
    private String ssedateStart;
    private String ssedateEnd;
    private String securityType = "all";
    private String bulletinType = "ALL";
    private String token = "APPMQUERY";
    private String securityName;
    private String securityCode;
}
