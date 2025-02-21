package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class KcbJygkListReqBody {

    private String tradeDate;
    private String secCode;
    private String flag;
    private Integer pageSize = 0;
    private String token = "APPMQUERY";

}
