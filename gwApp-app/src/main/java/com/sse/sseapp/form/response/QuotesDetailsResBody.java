package com.sse.sseapp.form.response;

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
public class QuotesDetailsResBody extends RespContentVO {
    private List<Trd1Bean> list = Lists.newArrayListWithExpectedSize(10);

    @Data
    public static class Trd1Bean {
        private String time;
        private String value;
        private String count;
    }
}
