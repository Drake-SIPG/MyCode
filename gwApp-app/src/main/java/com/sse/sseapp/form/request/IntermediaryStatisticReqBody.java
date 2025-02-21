package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class IntermediaryStatisticReqBody {

    private String token = "APPMQUERY";

    private String type;

    private String order;

    private Integer page;

    private Integer pageSize;

    private String issueMarketType;
}
