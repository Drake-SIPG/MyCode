package com.sse.sseapp.form.response;

import com.google.common.collect.Lists;
import com.sse.sseapp.app.core.domain.RespContentVO;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MarketDailyTransactionOverviewResBody extends RespContentVO {
    private List<Item> list = Lists.newArrayList();

    @Data
    public static class Item {
        /**
         * 分类
         */
        private String name;
        /**
         * 挂牌数
         */
        private String listNum;
        /**
         * 总市值
         */
        private String totalValue;
        /**
         * 流通市值
         */
        private String negoValue;
        /**
         * 成交笔数
         */
        private String volume;
        /**
         * 成交金额
         */
        private String amount;
        /**
         * 加权平均价
         */
        private String avgPrice;
        /**
         * 成交量
         */
        private String tradeVol;
        /**
         * 成交金额
         */
        private String tradeAmt;
        /**
         * 融资余额(元)
         */
        private String rzye;
        /**
         * 融券余量金额(元)
         */
        private String rqylje;
        /**
         * 融券余量
         */
        private String rqyl;
        /**
         * 融资买入额(元)
         */
        private String rzmre;
        /**
         * 融资融券余额(元)
         */
        private String rzrqjyzl;
        /**
         * 融券卖出量
         */
        private String rqmcl;

        /**
         * 平均市盈率
         */
        private String avgPeRate;

        /**
         * 换手率
         */
        private String totalRate;

        /**
         * 流通换手率
         */
        private String neGoToRate;
    }
}
