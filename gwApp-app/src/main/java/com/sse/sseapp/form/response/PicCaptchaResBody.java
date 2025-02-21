package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PicCaptchaResBody extends RespContentVO {
    /**
     * session
     */
    private String session;
    /**
     * 图片验证码
     */
    private String picCaptcha;

}
