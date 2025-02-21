package com.sse.sseapp.form.request;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class RiskStockListReqBody {
    private String token = "APPMQUERY";

    private String pageSize;
    private String domesticIndicator;

    private String pageNo;

    private String productType;

    @JsonGetter("pageSize")
    public String getPageSize() {
        return pageSize;
    }

    @JsonSetter("pageSize")
    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    @JsonGetter("pageNo")
    public String getPageNo() {
        return pageNo;
    }

    @JsonSetter("page")
    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }
}
