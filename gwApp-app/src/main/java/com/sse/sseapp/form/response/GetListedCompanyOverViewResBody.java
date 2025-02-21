package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/24 15:56
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetListedCompanyOverViewResBody extends RespContentVO {


    /**
     * peRate : 55.51
     * negCpt : 1102.68
     * mdate : 202206
     * csrccode : A
     * negVol : 143.57
     * stkVol : 16
     * mktCpt : 1251.45
     * trdVol : 113.24
     * csrcname : 农、林、牧、渔业
     * issVol : 151.62
     * trdVal : 1125.80
     * toRate : 87.32
     */

    private String peRate;
    private String negCpt;
    private String mdate;
    private String csrccode;
    private String negVol;
    private int stkVol;
    private String mktCpt;
    private String trdVol;
    private String csrcname;
    private String issVol;
    private String trdVal;
    private String toRate;

}
