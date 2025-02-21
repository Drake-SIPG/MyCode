package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class RegisterReqBody extends ReqContentVO {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码
     */
    private String password;
    /**
     * 短信验证码
     */
    private String smsCaptcha;

}
