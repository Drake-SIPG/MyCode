package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class FindCompanyVideosReqBody extends ReqContentVO {
    /**
     * 发布ID (第一页时qid=0, 否则前一页最后一个qid)
     */
    private String qid = "0";

    private String pageSize = "20";

    private String fromApp = "APPREQUEST";

    private String method = "findCompanyVideos";
}
