package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/24 14:47
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetDataListReqBody extends ReqContentVO {
    /**
     * Query接口url,例如：http://query.sse.com.cn//commonQuery.do
     */
    private String url;

    /**
     * Query接口传参，多个参数用|分割，例如：sqlId=COMMON_SSE_ZQZS_M_SSE_INDEX_C|isPagination=false
     */
    private String params;
}
