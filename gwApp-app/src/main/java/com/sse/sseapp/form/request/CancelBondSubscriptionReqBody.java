package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class CancelBondSubscriptionReqBody extends ReqContentVO {
    private String[] bondCode;
}
