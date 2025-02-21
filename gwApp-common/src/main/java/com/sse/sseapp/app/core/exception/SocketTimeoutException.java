package com.sse.sseapp.app.core.exception;

import com.sse.sseapp.app.core.base.RespEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述具体功能。<br>
 *
 * @author wy
 * @date 2023/08/10
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@Data
public class SocketTimeoutException extends AppException {
    private final RespEnum resp;
}
