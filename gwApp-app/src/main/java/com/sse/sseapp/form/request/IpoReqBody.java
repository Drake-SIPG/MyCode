package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;


@Data
public class IpoReqBody extends ReqContentVO {

    private String token;
    private String listedStartDate;
    private String pageNo;
    private String pageSize;
    private String tradeMarket;
    private String order;
    private String date;
    private String code;
    private String uid;
    private String tradeBeginDate;
    private String tradeEndDate;
    private String stockAbbr;

}
