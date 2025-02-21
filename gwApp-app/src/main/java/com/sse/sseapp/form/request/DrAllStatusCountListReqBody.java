package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liangjm
 * @create-date: 2023/12/28 16:49
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class DrAllStatusCountListReqBody extends ReqContentVO {
    private String issueMarketType;
    private String token = "APPMQUERY";
}
