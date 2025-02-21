package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class CompanyPublishReqBody extends ReqContentVO {

    /**
     * 发布ID (第一页时qid=0, 否则前一页最后一个qid)
     */
    private String qid = "-1";

    /**
     * 	1为全选 0为自选
     */
    private Integer codeType;

    private String type = "30";

    private String pageSize = "20";

    private String stockCode;
}
