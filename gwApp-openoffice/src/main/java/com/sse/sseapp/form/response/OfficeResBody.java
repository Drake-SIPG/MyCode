package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 文件转换结果输出
 *
 * @author wy
 * @date 2023-06-02
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class OfficeResBody extends RespContentVO {

    @JsonProperty("fileUrl")
    private String fileUrl;
}
