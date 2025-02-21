package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * 债券做市-新债券交易系统综合排名
 *
 * @author wy
 * @date 2023-08-09
 */
@Data
public class BondRankingReqBody extends ReqContentVO {

    private String type;

}
