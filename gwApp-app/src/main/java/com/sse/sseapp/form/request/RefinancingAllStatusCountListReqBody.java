package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/26 16:17
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class RefinancingAllStatusCountListReqBody extends ReqContentVO {
    private String issueMarketType;
    private String token = "APPMQUERY";
}
