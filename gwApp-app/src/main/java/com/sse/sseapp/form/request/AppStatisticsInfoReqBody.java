package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class AppStatisticsInfoReqBody extends ReqContentVO {

    /**
     * 电话
     */
    private String phone;
}
