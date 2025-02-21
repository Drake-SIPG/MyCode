package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/13 14:16
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetRoadShowListByPageReqBody extends ReqContentVO {

    private String stockCode;

    private String pageIndex = "1";

    private String pageSize ="20";

}
