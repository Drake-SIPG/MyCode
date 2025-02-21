package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/13 10:04
 */
@Data
public class ShareholdersMeetingListReqBody extends ReqContentVO {

    private String stockCode;

    private String stockAbbr;

    private String tradeBeginDate;

    private String tradeEndDate;

    private Boolean isSelf;

    private String bizType = "4";

    private String order = "stockCode";

}
