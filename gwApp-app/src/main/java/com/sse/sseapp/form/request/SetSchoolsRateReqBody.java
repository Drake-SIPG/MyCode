package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class SetSchoolsRateReqBody extends ReqContentVO {

    private String studypagename;
    private String section;
}
