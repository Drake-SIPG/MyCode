package com.sse.sseapp.form.response;

import lombok.Data;

/**
 * 债券做市-年度优秀做市商输出对象
 *
 * @author wy
 * @date 2023-08-08
 */
@Data
public class MarketMakingYearResBody {

    /**
     * 序号
     */
    private String rown;

    /**
     * 开始日期，格式“YYYYMMDD”
     */
    private String startDate;

    /**
     * 结束日期，格式“YYYYMMDD”
     */
    private String endDate;

    /**
     * 做市商名称
     */
    private String firmName;

    /**
     * 做市商最终评价得分
     */
    private String firmScore;

}
