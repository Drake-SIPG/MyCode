package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ShareholderMeetingListReqBody extends ReqContentVO {

    //股票代码
    private String secCode;
    //日期
    private String conveneDate;

    private String sqlId = "COMMON_SSE_SCFW_TZZFW_GDDHWLTPZL_L";
}
