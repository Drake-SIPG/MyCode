package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 9:18
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetListingDataListReqBody extends ReqContentVO {
    /**
     * Query接口url,例如：http://query.sse.com.cn//commonQuery.do
     */
    private String url;

    /**
     * Query接口传参，多个参数用|分割，例如：sqlId=COMMON_SSE_ZQZS_M_SSE_INDEX_C|isPagination=false
     */
    private String params;

    /**
     * 1：首页合并接口，2：项目动态接口
     */
    private String reuqestType;

    /**
     * 用于区分当reuqestType=2时，调用的是哪个项目动态接口，在当前项目状态翻译时调用对应的方法
     */
    private String sqlId;

    private String passId;
}
