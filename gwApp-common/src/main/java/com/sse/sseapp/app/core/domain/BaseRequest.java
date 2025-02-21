package com.sse.sseapp.app.core.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseRequest<T> {

    // 基础信息，存放app版本，os版本，uuid等信息
    @NotNull(message = "base不能为null")
    private ReqBaseVO base;

    // 请求体
    @NotNull(message = "reqContent不能为null")
    @Valid
    private T reqContent;
}
