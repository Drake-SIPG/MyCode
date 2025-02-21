package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/31 10:10
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ChannelCategoryDataReqBody extends ReqContentVO {
    private String type = "22";
    private String token = "APPMQUERY";
}
