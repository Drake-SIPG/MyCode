package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class MarginTradingNoticeReqBody extends ReqContentVO {
    private String siteId = "28";
    private String channelId = "8432,8433";
    private String pageNo = "1";
    private String pageSize = "4";
    private String order = "createTime|desc,docId|desc";

}
