package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class MarketCalendarListReqBody {

    private String bizSeq;
    private String stockCode;
    private String stockAbbr;
    private String tradeBeginDate;
    private String tradeEndDate;
    private String bizType;
    private String order = "ext1|asc,stockCode|asc,tradeBeginDate|asc";
    private Integer pageNo;
    private Integer pageSize;
    private Boolean self = false;
    private String type;
}
