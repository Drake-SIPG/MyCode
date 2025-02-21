package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class SearchHasDataReqBody extends ReqContentVO {
    private String searchword;
    private String beginDate;
    private String endDate;
    private String keywordPosition;
    private String orderByKey;
}
