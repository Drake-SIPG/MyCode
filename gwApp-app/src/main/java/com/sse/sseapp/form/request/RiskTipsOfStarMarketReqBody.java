package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class RiskTipsOfStarMarketReqBody {
    private String type;
    private Integer pageNo = 1;
    private Integer pageSize = 0;
    private String token = "APPMQUERY";
}
