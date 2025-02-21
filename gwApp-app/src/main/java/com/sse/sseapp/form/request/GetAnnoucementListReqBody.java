package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/14 9:12
 */
@Data
public class GetAnnoucementListReqBody extends ReqContentVO {

    private String type;

    private String channelId;

    private String pageSize = "20";

    private String page = "1";

    private Boolean isself = false;

    private String order = "createTime|desc,docId|desc";

    private String siteId = "28";

}
