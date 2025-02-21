package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/12 17:06
 */
@Data
public class InteractionListReqBody extends ReqContentVO {

    /**
     * 股票代码
     */
    private String stockCode;

    private String pageSize = "10";

    private String pageIndex = "1";

    private String type;


    //以下不需要前端传入默认

    private String method = "getCompanyLatestQAs";

    private String companyTag = "1";

//String type 1 提问   2答复  不传 全部
}
