package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class ScanLoginReqBody extends ReqContentVO {
    /**
     * 扫描二维码id
     */
    private String qrId;

}
