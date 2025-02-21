package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
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
public class OptionTrd2ResBody extends RespContentVO {
    private List<Trd2Bean> list = Lists.newArrayListWithExpectedSize(10);

    @Data
    public static class Trd2Bean {
        private String value;

        @JsonIgnore
        private String count;

        private String countStr;

        private String percent;
    }
}
