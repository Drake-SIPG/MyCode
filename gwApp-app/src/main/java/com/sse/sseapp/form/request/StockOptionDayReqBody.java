package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class StockOptionDayReqBody extends ReqContentVO {

    private String tradeDate;
}
