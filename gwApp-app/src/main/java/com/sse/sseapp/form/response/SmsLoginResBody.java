package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class SmsLoginResBody extends RespContentVO {

    /**
     * OAuth2 登录后分配的token
     */
    private String accessToken;
    /**
     * OAuth2 登录后分配的唯一 ID
     */
    private String email;
    /**
     * 通行证内部用户唯一 ID
     */
    private String image;
    /**
     * 用户绑定的邮箱账号
     */
    private String mobile;
    /**
     * 用户绑定的手机账号
     */
    private String nickname;
    /**
     * 用户昵称
     */
    private String passId;
    /**
     * 用户头像地址
     */
    private String unionId;
    /**
     * 绑定的微信 union_id，未绑定为空
     */
    private String wxUnionId;

}
