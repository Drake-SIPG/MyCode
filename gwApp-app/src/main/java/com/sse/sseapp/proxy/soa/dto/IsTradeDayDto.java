package com.sse.sseapp.proxy.soa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/22 17:33 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class IsTradeDayDto extends RespContentVO {
    private String tday;

    private String isTradeDay;
}
