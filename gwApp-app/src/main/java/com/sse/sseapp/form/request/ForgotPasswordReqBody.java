package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class ForgotPasswordReqBody extends ReqContentVO {
    /**
     * 用户名
     */
    private String loginName;
    /**
     * 短信验证码
     */
    private String mobileCode;
    /**
     * 密码
     */
    private String newPwd;


}
