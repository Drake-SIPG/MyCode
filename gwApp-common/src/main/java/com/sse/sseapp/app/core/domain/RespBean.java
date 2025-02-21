package com.sse.sseapp.app.core.domain;

import com.sse.sseapp.app.core.base.RespEnum;
import lombok.Data;

/**
 * 响应体
 */
@Data
public class RespBean<T> {

    private String status;
    private String msg;
    private T data;

    private RespBean() {
    }

    private RespBean(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static RespBean build() {
        return new RespBean();
    }

    public static RespBean success() {
        return new RespBean(RespEnum.SUCCESS.getCode(), RespEnum.SUCCESS.getMessage(), null);
    }

    public static RespBean success(String msg) {
        return new RespBean(RespEnum.SUCCESS.getCode(), msg, null);
    }

    public static RespBean success(RespContentVO obj) {
        return new RespBean(RespEnum.SUCCESS.getCode(), RespEnum.SUCCESS.getMessage(), obj);
    }

    public static RespBean success(Object object) {
        return new RespBean(RespEnum.SUCCESS.getCode(), RespEnum.SUCCESS.getMessage(), object);
    }

    public static RespBean success(String msg, RespContentVO obj) {
        return new RespBean(RespEnum.SUCCESS.getCode(), msg, obj);
    }

    public static RespBean error() {
        return new RespBean(RespEnum.ERROR.getCode(), RespEnum.ERROR.getMessage(), null);
    }

    public static RespBean error(String msg) {
        return new RespBean(RespEnum.ERROR.getCode(), msg, null);
    }

    public static RespBean error(RespEnum resp, String msg) {
        return new RespBean(resp.getCode(), msg, null);
    }

    public static RespBean error(RespContentVO obj) {
        return new RespBean(RespEnum.ERROR.getCode(), RespEnum.ERROR.getMessage(), obj);
    }

    public static RespBean error(String msg, RespContentVO obj) {
        return new RespBean(RespEnum.ERROR.getCode(), msg, obj);
    }

    /**
     * 返回解密失败信息
     *
     * @return
     */
    public static RespBean decrypt() {
        return new RespBean(RespEnum.DECRYPT.getCode(), RespEnum.DECRYPT.getMessage(), null);
    }

    public static RespBean decrypt(String msg) {
        return new RespBean(RespEnum.DECRYPT.getCode(), msg, null);
    }

    public static RespBean decrypt(RespContentVO obj) {
        return new RespBean(RespEnum.DECRYPT.getCode(), RespEnum.DECRYPT.getMessage(), obj);
    }

    public static RespBean decrypt(String msg, RespContentVO obj) {
        return new RespBean(RespEnum.DECRYPT.getCode(), msg, obj);
    }

    public static RespBean permission() {
        return new RespBean(RespEnum.PERMISSION.getCode(), RespEnum.PERMISSION.getMessage(), null);
    }

}
