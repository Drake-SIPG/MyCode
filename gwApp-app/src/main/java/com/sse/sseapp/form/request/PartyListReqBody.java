package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class PartyListReqBody {

    private String type;
    private Integer pageNo = 1;
    private Integer pageSize = 5;
    private String order = "createTime|desc,docId|desc";

}
