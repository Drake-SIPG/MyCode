package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import java.util.List;
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
public class SearchStockResBody extends RespContentVO {
    private List<DataDTO> list;

    @Data
    public static class DataDTO {
        @JsonProperty("SUB_TYPE")
        private String SUB_TYPE;
        @JsonProperty("SEC_CODE")
        private String SEC_CODE;
        @JsonProperty("SEC_NAME_FULL")
        private String SEC_NAME_FULL;
        @JsonProperty("SEC_TYPE")
        private String SEC_TYPE;
    }
}
