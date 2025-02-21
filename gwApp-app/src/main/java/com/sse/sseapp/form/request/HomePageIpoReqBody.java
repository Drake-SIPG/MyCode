package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;


@Data
public class HomePageIpoReqBody extends ReqContentVO {

    private String onlineIssuanceStartDate;
    private String onlineIssuanceEndDate;
    private String stockType;
    private String pageSize;
    private String token = "APPMQUERY";
    private String tradeMarket = "SH";
    private String order = "onlineIssuanceDate|asc,id|asc";
}
