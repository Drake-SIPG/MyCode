package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class ProjectStatisticInfoReqBody {

    private String issueMarketType;
    private String token = "APPMQUERY";
    private Integer page;
    private Integer pageSize;
}
