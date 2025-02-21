package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class AddFavouriteReqBody extends ReqContentVO {

    private String docId;
    private Integer userId;
    private String docType;
    private String docTitle;
    private String docURL;
    private String docPublishTime;
    private String addedChannel = "1";
    private String addedTime;
    private Integer deviceType;
    private String deviceId;
    private String stockCode;
    private String stockName;


}
