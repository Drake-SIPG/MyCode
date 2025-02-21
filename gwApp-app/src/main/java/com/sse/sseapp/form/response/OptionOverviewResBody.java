package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/31 15:04 hanjian 创建
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class OptionOverviewResBody extends RespContentVO {
    private String prevClose;
    private String open;
    private String high;
    private String low;
    private String last;
    private String change;
    private String chgRate;
    private String volume;
    private String amount;
    private String ampRate;
    private String turnoverRate;
    private String entrustRatio;
    private String avgPx;
    private String sellVol;
    private String buyVol;



}
