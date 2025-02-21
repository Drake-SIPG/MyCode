package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class GetPromiseListReqBody extends ReqContentVO {
    private String companyCode;
    private String boardType;
    private String token = "APPMQUERY";
    private String pageNo;
    private String pageSize = "20";
}
