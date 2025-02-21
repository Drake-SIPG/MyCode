package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import com.sse.sseapp.form.response.OptionalStockResBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class QuotesInfoReqBody extends ReqContentVO {
    private String type = "";

    private int begin = 0;

    private Boolean SHB1 = true;

    private String urlPrefix;

    private String orderName = "code";

    private String orderValue = "ase";

    private String uid;

    private boolean self = false;

    private String requestType = "name,code,last,chg_rate,volume,high,low,open,amount,amp_rate,prev_close,cpxxprodusta,cpxxsubtype,avg_px,hlt_tag,cpxxlmttype";

    private List<OptionalStockResBody> optionalstock;

}
