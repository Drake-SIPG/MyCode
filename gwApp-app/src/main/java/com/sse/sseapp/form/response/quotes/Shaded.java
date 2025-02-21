package com.sse.sseapp.form.response.quotes;

import cn.hutool.json.JSONObject;
import com.sse.sseapp.form.response.OptionalStockResBody;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/7/5 14:15
 */
@Data
public class Shaded {
    private String requestType = "name,code,last,chg_rate,volume,high,low,open,amount,amp_rate,prev_close,cpxxprodusta,cpxxsubtype,avg_px,hlt_tag,cpxxlmttype";
    private Boolean SHB1 = true;
    private String urlPrefix;
    private List<OptionalStockResBody> optionalstock;
    private String type = "";
    private int begin = 0;
    private String orderName = "code";
    private String orderValue = "ase";
}
