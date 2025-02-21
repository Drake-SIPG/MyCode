package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * 债券做市-自选做市品种输入对象
 *
 * @author wy
 * @date 2023-08-08
 */
@Data
public class MarketMakingSelfReqBody extends ReqContentVO {

    private Boolean isPagination = true;
    private Integer pageNo;
    private Integer pageSize;
    private String sqlId = "BOND_SY_ZQZS_ZXZSPZ";



}
