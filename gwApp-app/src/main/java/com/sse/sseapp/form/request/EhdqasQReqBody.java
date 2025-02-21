package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class EhdqasQReqBody extends ReqContentVO {
    /**
     * 当前页数
     */
    private String page = "1";

    /**
     *  最后一条数据id
     */
    private String lastid = "-1";

    /**
     * 	1为全选 0为自选
     */
    private Integer codeType;

    private String type = "10";

    private String pageSize = "20";

    private String show = "1";

    private String companyTag = "1";

    private String stockCode;

    private String method;


}
