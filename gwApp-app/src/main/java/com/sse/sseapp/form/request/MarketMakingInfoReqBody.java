package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * 债券做市-基准做市品种-做市品种调整信息输入对象
 *
 * @author wy
 * @date 2023-08-08
 */
@Data
public class MarketMakingInfoReqBody extends ReqContentVO {

    /**
     * 债券代码
     */
    @JsonProperty("bondCode")
    private String BOND_CODE;

    /**
     * 债券类型
     * DBT0000:信用债
     * INT0000:利率债
     */
    @JsonProperty("bondType")
    private String BOND_TYPE;

    /**
     * 调整方向
     * TR:调入
     * TC:调出
     */
    @JsonProperty("modDirect")
    private String MOD_DIRECT;

    private String sqlId = "COMMON_BOND_ZQZS_JZZSPZ_ZSPZTZXX_C";
}
