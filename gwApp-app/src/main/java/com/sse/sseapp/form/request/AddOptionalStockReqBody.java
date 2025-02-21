package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class AddOptionalStockReqBody extends ReqContentVO {

    private String uid;
    private String token = "APPMQUERY";
    private String stockcode;
    private String stocktype;
    private String productsubtype;
    private String stockname;
    private String stockpy;
    private String addedfrom = "1";
    private String deviceid = "";
    private String deviceType;
    private String sourcetype = "0";
    private String addedtime;
    private String tradeMarket = "SH";

//    <uid clientmapping="userId"></uid>
//			<token default="APPMQUERY"></token>
//			<stockcode required="true" clientmapping="COMPANY_CODE" type="number" len="6"></stockcode>
//			<stocktype required="false"></stocktype>
//			<productsubtype required="false"></productsubtype>
//			<stockname required="true" clientmapping="COMPANY_NAME"></stockname>
//			<stockpy required="true" clientmapping="PYM"></stockpy>
//			<addedfrom default="1"></addedfrom>
//			<deviceid default=""></deviceid>
//			<devicetype required="true" clientmapping="deviceType"></devicetype>
//			<sourcetype default="0"></sourcetype>
//			<addedtime required="false"></addedtime>
//			<tradeMarket default="SH" clientmapping="trademarket"></tradeMarket>


}
