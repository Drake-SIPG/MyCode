package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class NoticeReqBody extends ReqContentVO {

    private String siteId="28";
    private String channelId = "80";
    private Integer pageNo;
    private String stockcode;
    private String order = "createTime|desc,docId|desc";
    private Integer pageSize;
    private boolean self = false;
    private Integer uid;

//    <siteId default="28"></siteId>
//			<channelId clientmapping="type" default="8349" valuemapping='{"60":"12244","70":"8349","80":"12244,8349,7979,8229,8311,8033,8367,8368,8369,8373","12":"12145","13":"12244","14":"8349,12244","15":"8361","16":"9807,9808,9809,9810,9812,9813,9814,9818,9815,9816,9817,9819","17":"12716,12717","99":"8349,12244"}'></channelId>
//			<pageNo clientmapping="page"></pageNo>
//			<stockcode clientmapping="code"></stockcode>
//			<order default="createTime|desc,docId|desc"></order>
}
