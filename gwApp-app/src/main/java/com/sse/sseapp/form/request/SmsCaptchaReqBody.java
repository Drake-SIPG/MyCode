package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class SmsCaptchaReqBody extends ReqContentVO {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 短信类型
     * 短信类型->
     * 0：手机号注册，
     * 1：手机找回密码，
     * 2： 短信登录，
     * 3：绑定手机号，
     * 4：邮箱绑定，
     * 5： 股东卡号绑定。
     */
    private String type;
    /**
     * 图片验证码
     */
    private String picCaptcha;

    private String session;

}
