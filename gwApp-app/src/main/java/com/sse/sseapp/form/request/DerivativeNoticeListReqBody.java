package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class DerivativeNoticeListReqBody extends ReqContentVO {
    private String title;
    private String createTime;
    private String createTimeEnd;
    private String order = "createTime|desc,docId|desc";
    private String stockcode;
    private String pageNo;
    private String type = "17";
    private String pageSize;
    private String siteId = "28";
    private String channelId = "12716";

    @JsonSetter("sseDate")
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @JsonSetter("sseDateEnd")
    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    @JsonSetter("code")
    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
    }
}
