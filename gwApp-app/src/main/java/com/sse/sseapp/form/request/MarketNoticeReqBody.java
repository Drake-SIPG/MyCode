package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * 债券做市-债券公告输入对象
 *
 * @author wy
 * @date 2023-08-08
 */
@Data
public class MarketNoticeReqBody extends ReqContentVO {

    private String order = "sseDate|desc,securityCode|asc,bulletinId|asc";

    private String sqlId = "BS_ZQ_GGLL";

    /**
     * 每页条数
     */
    private Integer pageSize;

    private Boolean isPagination = true;
}
