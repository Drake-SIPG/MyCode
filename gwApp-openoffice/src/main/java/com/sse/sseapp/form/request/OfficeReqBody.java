package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 文件转换接口请求参数封装
 *
 * @author wy
 * @date 2023-06-02
 */
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
@Data
public class OfficeReqBody extends ReqContentVO {

    /**
     * 源文件地址
     */
    @NotNull(message = "源文件地址不能为空！")
    private String sourceFileUrl;
}
