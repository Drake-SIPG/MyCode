package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/25 17:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RefreshTokenResBody extends RespContentVO {
    private String accessToken;
}
