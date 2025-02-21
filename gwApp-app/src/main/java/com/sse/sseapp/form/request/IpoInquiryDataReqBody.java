package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class IpoInquiryDataReqBody {
    private Integer pageNo = 1;
    private Integer pageSize = 20;
    private String stockType;

}
