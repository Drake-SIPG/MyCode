package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mateng
 * @since 2023/7/19 14:32
 */
@NoArgsConstructor
@Data
public class ErrorMsgReqBody extends ReqContentVO {

    private String userName;
    private String devType;
    private String osType;
    private String version;
    private String method;
    private Msg msg;

    @NoArgsConstructor
    @Data
    static class Msg {

        /**
         * statusCode : 0
         * statusText :
         * request : {"requestParams":{"url":"http://10.8.16.49:80/app/stocktrade/getMarketData","method":"POST","header":{},"data":"{\"reqContent\":{\"productType\":\"科创板\",\"tradeDate\":\"max\"},\"base\":{}}","dataType":"JSON","timeout":30000,"promise":false}}
         * errMsg : request:fail error
         */

        private int statusCode;
        private String statusText;
        private RequestBean request;
        private String errMsg;

    }

    @NoArgsConstructor
    @Data
    static class RequestBean {

        /**
         * requestParams : {"url":"http://10.8.16.49:80/app/stocktrade/getMarketData","method":"POST","header":{},"data":"{\"reqContent\":{\"productType\":\"科创板\",\"tradeDate\":\"max\"},\"base\":{}}","dataType":"JSON","timeout":30000,"promise":false}
         */

        private RequestParamsBean requestParams;

    }

    @NoArgsConstructor
    @Data
    static class RequestParamsBean {

        /**
         * url : http://10.8.16.49:80/app/stocktrade/getMarketData
         * method : POST
         * header : {}
         * data : {"reqContent":{"productType":"科创板","tradeDate":"max"},"base":{}}
         * dataType : JSON
         * timeout : 30000
         * promise : false
         */

        private String url;
        private String method;
        private HeaderBean header;
        private String data;
        private String dataType;
        private int timeout;
        private boolean promise;

    }

    @NoArgsConstructor
    @Data
    static class HeaderBean {

    }
}


