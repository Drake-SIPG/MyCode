package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class SearchInfoReqBody extends ReqContentVO {
    private String page = "1";
    private String searchword;
    private String pageSize = "10";
    private String searchtarget;
    private String searchType;
    private String beginDate;
    private String endDate;
    private String keywordPosition;
    private String orderByKey;
    private String orderByDirection;
}
