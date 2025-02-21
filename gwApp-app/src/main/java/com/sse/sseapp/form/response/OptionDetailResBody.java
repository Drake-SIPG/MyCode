package com.sse.sseapp.form.response;

import com.google.common.collect.Lists;
import com.sse.sseapp.app.core.domain.RespContentVO;
import java.util.List;
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
public class OptionDetailResBody extends RespContentVO {
    private List<Data> left = Lists.newLinkedList();

    private List<Data> right = Lists.newLinkedList();

    @lombok.Data
    public static class Data {
        private String name;
        private String code;
        private String exepx;
        private String last;
        private String change;
        private String chgRate;
        private String amount;
        private String volume;
        private String openInterest;
        private String nowVol;
        private String ampRate;
        private String open;
        private String high;
        private String low;
        private String prevClose;
        private String avgpx;
        private String expiremonth;
        private String buyVol;
        private String sellVol;
        private String volatility;
        private String delta;
        private String vega;
        private String theta;
        private String gamma;
        private String rho;
        private String presetpx;
        private String time;
        private String tradephase;
    }
}
