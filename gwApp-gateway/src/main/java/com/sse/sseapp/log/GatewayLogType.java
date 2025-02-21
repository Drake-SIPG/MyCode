package com.sse.sseapp.log;

/**
 * 根据请求mediatype输出不同格式的日志
 * @author wangfeng
 * @date 2023/1/4 17:07
 **/
public interface GatewayLogType {

    /**
     * 请求类型为json
     */
    String APPLICATION_JSON_REQUEST = "applicationJsonRequest";

    /**
     * 请求类型为formData
     */
    String FORM_DATA_REQUEST = "formDataRequest";

    /**
     * 请求类型为formData 和 json 以外
     */
    String BASIC_REQUEST = "basicRequest";

    /**
     * 常规输出
     */
    String NORMAL_REQUEST = "normalRequest";

    /**
     * 慢查询
     */
    String SLOW = "slow";

    /**
     * 非200响应
     */
    String FAIL = "fail";
}
