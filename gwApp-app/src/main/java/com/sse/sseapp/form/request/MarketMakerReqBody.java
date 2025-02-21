package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * 债券做市-做市场输入对象
 *
 * @author wy
 * @date 2023-08-07
 */
@Data
public class MarketMakerReqBody extends ReqContentVO {

    /**
     * 做市类型
     * 0：主做市商
     * 1：一般做市商
     */
    @JsonProperty("zsType")
    private String ZS_TYPE;

    /**
     * 证券品种
     * GP：股票
     * JJ：基金
     * ZQ：债券
     * QQ：期权
     * DR：存托凭证
     */
    @JsonProperty("productType")
    private String PRODUCT_TYPE;

    private String sqlId = "COMMON_SSE_CP_JJ_JJZSSLB_ZSSLB_L";
}
