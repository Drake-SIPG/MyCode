package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/6 16:50
 */
@Data
public class PictureNewsReqBody extends ReqContentVO {

    private String siteId = "28";

    private String pageNo ="1";

    private String pageSize = "5";

    private String order = "createTime|desc,docId|desc";

    private String channelId = "10330";

}
