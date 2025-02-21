package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.sse.sseapp.app.core.domain.RespContentVO;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MarketTotalInfoDetailResBody extends RespContentVO {
    private List<ResultDTO> list = Lists.newArrayList();

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class ResultDTO {
        private String totalValue;
        private String securityNum;
        private String negoValue;
        private String totalTradeAmt;
        private String productName;
    }
}
