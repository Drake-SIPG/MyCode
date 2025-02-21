package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class KcbZlpsListReqBody {

    private String tradeDate;
    private Integer pageSize = 0;
    private Integer pageNo;
    private String token = "APPMQUERY";
}
