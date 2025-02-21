package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class AppBondSubscriptionReqBody extends ReqContentVO {
    private String bondCode;
    @Pattern(regexp = "^((\\+?86)|((\\+86)))?1\\d{10}$", message = "手机号码格式不正确")
    private String mobile;
}
