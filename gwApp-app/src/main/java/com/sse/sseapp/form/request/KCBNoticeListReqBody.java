package com.sse.sseapp.form.request;


import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class KCBNoticeListReqBody {

    private String type;
    private Integer pageNo;
    private Integer pageSize;

    @JsonSetter("page")
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
