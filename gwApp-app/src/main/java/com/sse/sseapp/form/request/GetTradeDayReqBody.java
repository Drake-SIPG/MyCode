package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetTradeDayReqBody extends ReqContentVO {
    /**
     * 相对于tday的偏移量，posIndex可取负数、正数。原则上posIndex不为0，当tday=0时，如果tday是交易日，则返回tday本身，如果tday是非交易日，则返回空。
     */
    private Integer posIndex = -1;
    /**
     * 日期(YYYYMMDD),基准日期。tday相对于自己的偏移量为0。
     */
    private String tday;
}
