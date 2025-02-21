package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class CalendarListReqBody extends ReqContentVO {

    private String code;
    private Boolean isTradeDay;
    private String date;
    private String datatype;
    private Boolean dataSelf = false;
    private Boolean isSelf;

//    <stockCode clientmapping="code"></stockCode>
//			<stockAbbr></stockAbbr>
//			<tradeBeginDate clientmapping="date" default="${date}"></tradeBeginDate>
//			<tradeEndDate clientmapping="date" default="${date}"></tradeEndDate>
//			<bizType default="9"></bizType>
//			<order default="ext1|asc,stockCode|asc,tradeBeginDate|asc"></order>

}
