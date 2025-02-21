package com.sse.sseapp.form.request;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class SupervisionListReqBody {

    private String siteId = "28";
    private String channelId;
    private String type;
    private Integer pageNo;
    private String stockcode;
    private String order = "createTime|desc,docId|desc";
    private Integer pageSize;


    @JsonGetter("pageNo")
    public Integer getPageNo() {
        return pageNo;
    }

    @JsonSetter("page")
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
