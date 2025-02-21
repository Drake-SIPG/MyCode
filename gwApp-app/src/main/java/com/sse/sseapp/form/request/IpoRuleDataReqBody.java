package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class IpoRuleDataReqBody {
    private Integer pageNo;
    private Integer securityType;
    private Integer pageSize = 20;

}
