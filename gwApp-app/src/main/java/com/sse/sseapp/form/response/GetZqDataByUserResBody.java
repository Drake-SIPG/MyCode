package com.sse.sseapp.form.response;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/4 17:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetZqDataByUserResBody extends RespContentVO {
    private String company_abbr;
    private String company_code;
    private String pztype;
    private String shareholder_card;
    private String zq_date;
    private String zq_no;
    private String zq_qty_t;
    private String jkr;
}
