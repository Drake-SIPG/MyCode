package com.sse.sseapp.proxy;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/21 9:56 hanjian 创建
 */
public class RetryException extends RuntimeException {
    public RetryException() {
    }

    public RetryException(String message) {
        super(message);
    }

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryException(Throwable cause) {
        super(cause);
    }

    public RetryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
