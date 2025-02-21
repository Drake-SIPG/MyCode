package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/4 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class TeachingVideoReqBody extends ReqContentVO {
    private Long rootId;
}
