package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class BondFundamentalsReqBody extends ReqContentVO {
    private String sqlId = "COMMON_SSEBOND_XXPL_ZQXX_KZHGSZQ_XQ_C";
    @JsonProperty("BOND_CODE")
    private String BOND_CODE;
}
