package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 债券做市-债券公告输出对象
 *
 * @author wy
 * @date 2023-08-08
 */
@Data
public class MarketNoticeResBody {

    /**
     * 日期
     */
    private String sseDate;

    /**
     * URL
     */
    private String url;

    /**
     * 标题
     */
    private String title;

    /**
     * 公告唯一标识符（公告制作客户端公告ID）
     */
    private String bulletinId;

}
