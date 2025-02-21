package com.sse.sseapp.form.response;

import lombok.Data;

@Data
public class OneClickLoginResBody {
    String inresponseto ;
    String systemtime;
    String resultCode ;
    String msisdn;
    String taskId;
    String isIPmatch;
    String operatortype;

}
