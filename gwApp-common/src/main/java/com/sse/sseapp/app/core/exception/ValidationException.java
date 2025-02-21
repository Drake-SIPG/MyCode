package com.sse.sseapp.app.core.exception;

public class ValidationException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String code = "500";

    /**
     * 错误提示
     */
    private String message = "校验失败";

    /**
     * 错误明细，内部调试错误
     * <p>
     * 和 一致的设计
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ValidationException() {
    }

    public ValidationException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ValidationException(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public ValidationException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ValidationException setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getCode() {
        return code;
    }
}
