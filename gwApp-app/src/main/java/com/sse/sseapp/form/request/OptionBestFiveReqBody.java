package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class OptionBestFiveReqBody extends ReqContentVO {
    private String code;
    private String type;
    private String productType;
    private String productSubType;
}
