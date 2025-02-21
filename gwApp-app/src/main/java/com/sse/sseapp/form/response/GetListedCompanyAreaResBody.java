package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/24 15:09
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetListedCompanyAreaResBody extends RespContentVO {
    /**
     * 地区：含“其他”及“合计”
     */
    private String area;

    /**
     * 仅发A股上市公司数量
     */
    private String a;

    /**
     * A（B）、H股
     */
    private String abh;

    /**
     * A、B股
     */
    private String ab;

    /**
     * 仅发B股
     */
    private String b;

    /**
     * 合计
     */
    private String tot;

    /**
     * 统计月份，格式"YYYYMM"
     */
    private String mdate;

    /**
     * 地区：含“其他”及“合计”
     * AREA 是 '其他'：2, '合计'：3, 其余返回1
     */
    private String area_order;


}
