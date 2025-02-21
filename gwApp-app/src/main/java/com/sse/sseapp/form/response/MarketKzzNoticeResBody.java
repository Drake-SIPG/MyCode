package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 债券做市-债券公告可转债输出对象
 *
 * @author wy
 * @date 2023-12-21
 */
@Data
public class MarketKzzNoticeResBody {

    /**
     * 日期
     */
    @JsonProperty("SSEDATE")
    private String sseDate;

    /**
     * URL
     */
    @JsonProperty("URL")
    private String url;

    /**
     * 标题
     */
    private String title;

}
