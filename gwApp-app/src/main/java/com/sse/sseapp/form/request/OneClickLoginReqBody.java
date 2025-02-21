package com.sse.sseapp.form.request;

import lombok.Data;

@Data
public class OneClickLoginReqBody {

    String appid;
    String token;


    String version = "3.5";
    String msgid;
    String systemtime;
    String strictcheck = "1";
    String expandparams;

    String sign;
    String encryptionalgorithm;
    String clientipmd5;
}
