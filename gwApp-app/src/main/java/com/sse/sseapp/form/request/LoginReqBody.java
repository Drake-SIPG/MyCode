package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReqBody extends ReqContentVO {
    @NotBlank(message = "登录名不能为空")
    private String loginName;

    @NotBlank(message = "密码不能为空")
    private String password;

}
