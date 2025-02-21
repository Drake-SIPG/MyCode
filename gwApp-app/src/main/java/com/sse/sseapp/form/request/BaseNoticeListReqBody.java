package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class BaseNoticeListReqBody extends ReqContentVO {
    private String params;

    private String url;
}
