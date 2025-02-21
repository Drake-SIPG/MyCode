package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/4 17:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetPhDataByUserResBody extends RespContentVO {
    private String company_abbr;
    private String company_code;
    private String ph_begin;
    private String ph_date;
    private String ph_end;
    private String ph_num;
    private String ph_qty_t;
    private String pztype;
    private String shareholder_card;
}
