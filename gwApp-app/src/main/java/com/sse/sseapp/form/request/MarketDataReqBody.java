package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class MarketDataReqBody {

    private String token = "APPMQUERY";

    private String productType;
}
