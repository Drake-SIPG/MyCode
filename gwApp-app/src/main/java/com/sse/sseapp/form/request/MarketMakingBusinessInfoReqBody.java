package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * 债券做市-基准做市业务情况输入对象
 *
 * @author wy
 * @date 2023-08-08
 */
@Data
public class MarketMakingBusinessInfoReqBody extends ReqContentVO {

    private Integer pageNo;
    private Integer pageSize;

    /**
     * 基准做市品种：利率债、信用债
     */
    private String product;

    private Boolean isPagination = true;

    private String sqlId = "BOND_SY_ZQZS_JZZSYWQK";
}
