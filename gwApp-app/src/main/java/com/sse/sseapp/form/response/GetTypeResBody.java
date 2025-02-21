package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetTypeResBody extends RespContentVO {
    @JsonProperty("CATEGORY_PARENT_CODE")
    private String CATEGORY_PARENT_CODE;
    @JsonProperty("CATEGORY_CODE")
    private String CATEGORY_CODE;
    @JsonProperty("CATEGORY_NAME")
    private String CATEGORY_NAME;
}
