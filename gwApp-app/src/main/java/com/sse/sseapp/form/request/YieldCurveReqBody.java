package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * 基准指标-收益率曲线
 *
 * @author wy
 * @date 2023-08-10
 */
@Data
public class YieldCurveReqBody extends ReqContentVO {

    private String sqlId = "COMMON_BOND_SCSJ_JZZB_SYLQX_YHJGZ_L";
}
