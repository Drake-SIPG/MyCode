package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/6/28 15:48
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class OptionOverviewDtoResBody extends RespContentVO {
    private String contractid;
    private String stockid;
    private String code;
    private String name;
    private String prev_close;
    private String last;
    private String change;
    private String chg_rate;
    private String open;
    private String high;
    private String low;
    private String volume;
    private String now_vol;
    private String bid;
    private String ask;
    private String uplimit;
    private String downlimit;
    private String open_interest;
    private String s_volatility;
    private String delta;
    private String vega;
    private String theta;
    private String gamma;
    private String rho;
    private String amount;
    private String avg_px;
    private String exepx;
    private String unit;
    private String stocklast;
    private String premium;
    private String time_val;
    private String startdate;
    private String expdate;
    private String remaindate;
    private String in_val;
    private String y_volatility;
}
