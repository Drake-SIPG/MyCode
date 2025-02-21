package com.sse.sseapp.form.response;

import lombok.Data;

/**
 * 债券做市-自选做市品种输出对象
 *
 * @author wy
 * @date 2023-08-07
 */
@Data
public class MarketMakingSelfResBody {

    /**
     * 序号
     */
    private String rown;

    /**
     * 债券代码
     */
    private String bondCode;

    /**
     * 债券简称
     */
    private String bondAbbr;

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
}
