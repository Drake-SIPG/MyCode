package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * app消息设备绑定-入参
 *
 * @author wy
 * @date 2023-06-07
 */
@Data
public class AppMessageDeviceReqBody extends ReqContentVO {
    /**
     * 消息id
     */
    @NotBlank(message = "消息id不能为空")
    private String messageId;
}
