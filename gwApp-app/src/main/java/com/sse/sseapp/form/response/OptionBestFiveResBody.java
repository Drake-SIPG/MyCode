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
public class OptionBestFiveResBody extends RespContentVO {
    private List<BestFiveBean> buy = Lists.newArrayListWithExpectedSize(5);
    private List<BestFiveBean> sell = Lists.newArrayListWithExpectedSize(5);
    private String buyPercent = "";
    private String sellPercent = "";

    @Data
    public static class BestFiveBean {
        private String value;
        private String count;
    }
}
