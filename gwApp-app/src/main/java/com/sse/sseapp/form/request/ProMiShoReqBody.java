package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class ProMiShoReqBody extends ReqContentVO {

    private String productId;
    private Integer page;
    private String boardType;
}
