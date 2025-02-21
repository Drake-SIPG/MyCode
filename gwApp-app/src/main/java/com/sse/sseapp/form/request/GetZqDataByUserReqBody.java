package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/4 17:42
 */
@Data
public class GetZqDataByUserReqBody extends ReqContentVO {
    private String msgId;

    private String pageNo = "1";

    private String pageSize = "20";

    private String method = "zqDataByUserNew";

    private String order = "zq_date|desc,shareholder_card|asc";

}
