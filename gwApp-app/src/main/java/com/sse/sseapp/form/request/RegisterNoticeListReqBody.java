package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class RegisterNoticeListReqBody {

    private String type;
    private String docId;
    private String docTitle;
    private Integer pageNo = 1;
    private String order = "createTime|desc,docId|desc";
    private Integer pageSize;

}
