package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class IsOptionalStockReqBody extends ReqContentVO {

    private String uid;
    private String token = "APPMQUERY";
    private String stocktype;
    private String productsubtype;
    private String tradeMarket = "SH";
    private String accessToken;
    private Integer pageSize;
    private Integer pageNo;
    private String stockCode;
}
