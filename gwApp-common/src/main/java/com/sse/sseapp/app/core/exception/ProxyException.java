package com.sse.sseapp.app.core.exception;

import com.sse.sseapp.app.core.base.RespEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/4/27 10:07 hanjian 创建
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@Data
public class ProxyException extends AppException {
    private final RespEnum resp;

    private final String msg;
}
