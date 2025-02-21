package com.sse.sseapp.proxy;

import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.domain.system.SysProxyConfig;
import java.util.Map;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import static cn.hutool.core.map.MapUtil.newHashMap;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/16 15:04 hanjian 创建
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProxyConfig extends SysProxyConfig {
    private Map<String, Object> data = newHashMap();
    private ReqBaseVO base;

    public boolean encrypt() {
        return Objects.equals("1", getEncryption());
    }
}
