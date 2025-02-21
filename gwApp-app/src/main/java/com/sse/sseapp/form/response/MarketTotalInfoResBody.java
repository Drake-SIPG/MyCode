package com.sse.sseapp.form.response;

import com.google.common.collect.Lists;
import com.sse.sseapp.app.core.domain.RespContentVO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MarketTotalInfoResBody extends RespContentVO {
    private List<Item> numData = Lists.newArrayList();
    private List<Item> totalValueData = Lists.newArrayList();
    private List<Item> negoValueData = Lists.newArrayList();
    private List<Item> totalTradeAmtData = Lists.newArrayList();

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Item {
        private String name;

        private String value;
    }
}
