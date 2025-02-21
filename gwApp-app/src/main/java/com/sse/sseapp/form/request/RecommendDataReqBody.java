package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class RecommendDataReqBody extends ReqContentVO {

    /**
     * 页码
     */
    private String pageNo;
    private String sseNewsDocId;
    private String token = "APPMQUERY";
    private String pageSize = "0";
    private String tradeMarket = "SH";
    private String order = "onlineIssuanceDate|asc,id|asc";
    private String stockType;
//    <token default="APPMQUERY"></token>
//			<pageNo clientmapping="page" default="1"></pageNo>
//			<pageSize default="0"></pageSize>
//			<tradeMarket default="SH"></tradeMarket>
//			<order default="onlineIssuanceDate|asc,id|asc"></order>

}
