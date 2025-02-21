package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 信息披露公司公告接口请求参数封装
 *
 * @author zhengyaosheng
 * @date 2023-03-30
 */

@Data
public class CompNoticeReqBody extends ReqContentVO {

    /**
     * 页码
     */
    @NotNull(message = "pageNo不能为空！")
    private Integer pageNo;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 公告类型
     */
    private String bulletinType;

    /**
     * 板块类型，ALL：主板+科创板 zb:主板 kcb:科创板，默认是ALL
     */
    @NotNull(message = "securityType不能为空！")
    private String securityType;

    private String ssedateEnd;

    private String ssedateStart;

    private String title;

    private String securityName;

    private String securityCode;

}
