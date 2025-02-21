package com.sse.sseapp.app.core.base;

/**
 * 响应错误枚举
 */
public enum RespEnum {

    SUCCESS("200", "成功"),
    ERROR("15000", "失败"),
    SYSERROR("16000", "系统处理失败"),
    DECRYPT("14030", "解密失败"),
    NO_LOGIN("14010", "未登录"),
    TOKEN_INVALID("14020", "登录信息无效，请重新登录！"),
    PERMISSION("14040", "用户状态校验失败"),
    LOGIN_INFO_INVALID("14050", "登录信息已过期，请重新登录！"),
    ACCESSTOKEN_OVERDUW("14060", "登录信息已过期，请重新登录！");

    // 错误码
    private String code;
    // 错误描述
    private String message;

    /**
     * @param code    错误码
     * @param message 错误说明
     */
    RespEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
