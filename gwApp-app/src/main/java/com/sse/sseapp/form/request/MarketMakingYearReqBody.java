package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * 债券做市-年度优秀做市商输入对象
 *
 * @author wy
 * @date 2023-08-08
 */
@Data
public class MarketMakingYearReqBody extends ReqContentVO {

    /**
     * 查询日期，格式YYYYMMDD
     */
    private String searchDate;

    /**
     * 基准做市品种：利率债、信用债
     */
    private String product;

    private String sqlId = "BOND_SY_ZQZS_NDYXZSS";
}
