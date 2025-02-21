package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class PopupReqBody extends ReqContentVO {

    /**
     * 查询强制弹窗返回的时间戳
     */
    private String cacheTime;
}
