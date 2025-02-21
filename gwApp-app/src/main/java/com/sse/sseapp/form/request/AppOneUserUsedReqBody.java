package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class AppOneUserUsedReqBody extends ReqContentVO {

    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "业务子类格式不正确")
    @Size(min = 1, max = 10, message = "业务子类不能超过10个字符")
    private String navId;

    @Pattern(regexp = "^((\\+?86)|((\\+86)))?1\\d{10}$", message = "手机号码格式不正确")
    private String mobile;
}
