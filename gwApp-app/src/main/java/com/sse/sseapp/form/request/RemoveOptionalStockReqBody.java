package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class RemoveOptionalStockReqBody extends ReqContentVO {

    private Integer uid;
    private String token="APPMQUERY";
    private String companyCode;



//          <uid clientmapping="userId" required="true"></uid>
//			<token default="APPMQUERY"></token>
//			<stockcode required="true" clientmapping="code"></stockcode>
//			<stocktype required="false"></stocktype>
//			<productsubtype required="false"></productsubtype>
//			<tradeMarket clientmapping="trademarket"></tradeMarket>
}
