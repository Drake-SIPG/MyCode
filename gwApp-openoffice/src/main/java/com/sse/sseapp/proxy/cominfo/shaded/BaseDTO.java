package com.sse.sseapp.proxy.cominfo.shaded;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import lombok.Data;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/29 11:34 hanjian 创建
 */
@SuppressWarnings("all")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BaseDTO extends ReqBaseVO {
}
