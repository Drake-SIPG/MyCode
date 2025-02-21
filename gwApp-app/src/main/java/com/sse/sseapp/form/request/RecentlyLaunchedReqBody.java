package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class RecentlyLaunchedReqBody extends ReqContentVO {
    private String listingDateDesc = "1";
    private String sqlId = "COMMON_JJZWZ_SY_ZXJJ_JQFXSSJJ_L";
}
