package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class AppOneUserRecordReqBody extends ReqContentVO {

    @Pattern(regexp = "^[A-Za-z0-9,]+$", message = "业务类型格式不正确")
    @Size(min = 1, max = 100, message = "业务类型不能超过100个字符")
    private String businessType;

    @Pattern(regexp = "^((\\+?86)|((\\+86)))?1\\d{10}$", message = "手机号码格式不正确")
    private String mobile;
}
