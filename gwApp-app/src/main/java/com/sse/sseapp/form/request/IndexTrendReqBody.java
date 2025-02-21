package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * 基准指标-指数走势
 *
 * @author wy
 * @date 2023-08-09
 */
@Data
public class IndexTrendReqBody extends ReqContentVO {

    /**
     * 国债指数：sh1
     * 企债指数：csip
     * 沪公司债：sh1
     * 沪企债30：sh1
     */
    private String market;

    /**
     * 000012:国债
     * 000013:企债
     * 000022:沪公司债
     * 000061:沪企债30
     */
    private String debtType;

    private Integer begin = 0;

    private Integer end = -1;
}
