package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScanLoginResBody extends RespContentVO {
    /**
     * 200: 操作成功
     * 500: 二维码失效，请重新扫码
     * 501: 该二维码被扫描中，请稍后再试
     * 502: 未获取到用户信息，请确认登录后扫码
     * 503: 登录已失效，请重新登录
     */
    private String data;

}
