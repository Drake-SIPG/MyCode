package com.sse.sseapp.app.core.exception;

import com.sse.sseapp.app.core.base.RespEnum;
import com.sse.sseapp.app.core.domain.RespContentVO;

/**
 * 解密失败异常
 */
public class DecryptException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String code = RespEnum.DECRYPT.getCode();

    /**
     * 错误提示
     */
    private String message = RespEnum.DECRYPT.getMessage();

    /**
     * 错误明细，内部调试错误
     * <p>
     * 和 一致的设计
     */
    private String detailMessage;

    private RespContentVO vo;

    /**
     * 空构造方法，避免反序列化问题
     */
    public DecryptException() {
    }

    public DecryptException(String code) {
        this.code = code;
    }

    public DecryptException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public DecryptException(RespContentVO vo) {
        this.vo = vo;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public DecryptException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public DecryptException setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getCode() {
        return code;
    }

    public RespContentVO getVo() {
        return vo;
    }

    public void setVo(RespContentVO vo) {
        this.vo = vo;
    }
}
