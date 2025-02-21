package com.sse.sseapp.core.constant;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangfeng
 * @date 2022/12/29 11:42
 **/
@Data
public class ResponseBean implements Serializable {

    private static final long serialVersionUID = 1L;
    //成功code
    public static final String DEFAULT_SUCCESS_CODE = "1";
    //失败code
    public static final String DEFAULT_ERROR_CODE = "0";
    //返回code
    private String resCd;
    //提示信息
    private String resMsg;
    //返回数据
    private Object data;

    public ResponseBean() {
        this.resCd = DEFAULT_SUCCESS_CODE;
        this.resMsg = "成功";
        this.data = new Object();
    }


    public ResponseBean(String resCd, String resMsg, Object data) {
        this.resCd = resCd;
        this.resMsg = resMsg;
        this.data = data;
    }

    /**
     * @param resMsg 消息提示
     * @param object 返回数据
     * @return
     */
    public static ResponseBean success(String resMsg, Object object) {
        return new ResponseBean(DEFAULT_SUCCESS_CODE, resMsg, object);
    }

    /**
     * @param data 数据
     * @return
     */
    public static ResponseBean success(Object data) {
        return new ResponseBean(DEFAULT_SUCCESS_CODE, "成功", data);
    }

    /**
     * @param resCd  代码
     * @param resMsg 错误信息
     * @return
     */
    public static ResponseBean error(String resCd, String resMsg) {
        return new ResponseBean(resCd, resMsg, null);
    }

    /**
     * @param resMsg 消息提示
     * @param object 返回数据
     * @return
     */
    public static ResponseBean error(String resMsg, Object object) {
        return new ResponseBean(DEFAULT_ERROR_CODE, resMsg, object);
    }

    /**
     * @param resMsg 消息提示
     * @return
     */
    public static ResponseBean error(String resMsg) {
        return new ResponseBean(DEFAULT_ERROR_CODE, resMsg, null);
    }

    /**
     * @param resultCodeMsg 消息提示
     * @return
     */
    public static ResponseBean error(ResultCodeMsg resultCodeMsg) {
        return new ResponseBean(resultCodeMsg.getErrCode(), resultCodeMsg.getErrMsg(), null);
    }

    /**
     * @return
     */
    public static ResponseBean error() {
        return new ResponseBean(DEFAULT_ERROR_CODE, "失败", null);
    }
}
