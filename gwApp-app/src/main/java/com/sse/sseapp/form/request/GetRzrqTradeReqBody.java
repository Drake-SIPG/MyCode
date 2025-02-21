package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/24 17:12
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetRzrqTradeReqBody extends ReqContentVO {
    /**
     * 日期，格式"YYYYMM "
     */
    private String mdate;

    /**
     * 每页展示数量
     */
    private String pageSize = "0";

    /**
     * 页码
     */
    private String pageNo = "1";

    /**
     * 组合排序条件，多个排序条件用逗号分隔，排序字段和排序类型用竖划线’|’分隔，排在前面的字段排序级别越高。支持所有输出参数排序，排序类型为：desc 、asc
     */
    private String order;

    private String token = "APPMQUERY";
}
