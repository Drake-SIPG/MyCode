package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class SseVoicesReqBody extends ReqContentVO {

    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 类型
     */
    private Integer noticeType;

    /**
     * 数量
     */
    private Integer pageSize;

}
