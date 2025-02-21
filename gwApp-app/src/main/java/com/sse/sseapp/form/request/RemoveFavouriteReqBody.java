package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class RemoveFavouriteReqBody extends ReqContentVO {

    private Integer userId;
    private String docId;
    private String docType;
    private String docTitle;
    private String docURL;
}
