package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/13 15:02
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetBlockTradeDataReqBody extends ReqContentVO {

    private String toekn = "APPMQUERY";

    private String stockId;

    private String pageSize = "20";

    private String page = "1";

    private String tradeType;

    private String order="tradeDate|desc,tradePrice|desc";

    private String tradeBeginDate;

    private String tradeEndDate;

    private String abbrName;

    private String ifzc;

}
