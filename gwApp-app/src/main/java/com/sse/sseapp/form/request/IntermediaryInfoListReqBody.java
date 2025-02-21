package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class IntermediaryInfoListReqBody {

    private Integer pageNo = 1;
    private Integer pageSize = 0;
    private String type = "0";
    private String token = "APPMQUERY";
    private String siteId = "45";
}
