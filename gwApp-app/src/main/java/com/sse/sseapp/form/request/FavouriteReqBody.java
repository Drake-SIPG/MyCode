package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class FavouriteReqBody extends ReqContentVO {

    private Integer userId;
    private String docId;
    private String order = "addedTime|desc";
    private Integer pageNo;
    private Integer pageSize;




}
