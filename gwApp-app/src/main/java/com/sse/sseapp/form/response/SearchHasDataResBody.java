package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/31 15:04 hanjian 创建
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class SearchHasDataResBody extends RespContentVO {
    @JsonProperty("dataResult")
    private Boolean dataResult;
    @JsonProperty("newsResult")
    private Boolean newsResult;
    @JsonProperty("noticeResult")
    private Boolean noticeResult;
    @JsonProperty("ruleResult")
    private Boolean ruleResult;
    @JsonProperty("supervisionResult")
    private Boolean supervisionResult;
}
