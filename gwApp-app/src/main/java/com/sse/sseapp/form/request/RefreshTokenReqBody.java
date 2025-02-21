package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/25 17:14
 *
 */
@Data
public class RefreshTokenReqBody extends ReqContentVO {
    private String accessToken;
}
