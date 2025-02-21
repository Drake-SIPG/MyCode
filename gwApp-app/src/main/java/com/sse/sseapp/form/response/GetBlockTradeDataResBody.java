package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/13 19:20
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetBlockTradeDataResBody extends RespContentVO {
    private String abbrName;
    private String branchBuy;
    private String branchSell;
    private String ifzc;
    private String stockId;
    private String tradeAmount;
    private String tradeDate;
    private String tradePrice;
    private String tradeQty;
}
