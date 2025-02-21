package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/24 16:36
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetMemberTradeResBody extends RespContentVO {
    /**
     * 统计月份，格式"YYYYMM"
     */
    private String mdate;
    /**
     * 地区,包含一条为“合计”的记录
     */
    private String area;
    /**
     * 会员数
     */
    private String memberNum;
    /**
     * 地区：含“其他”及“合计”
     * AREA 是 '其他'：2, '合计'：3, 其余返回1
     */
    private String areaOrder;
    /**
     * 总计交易金额（亿元）
     */
    private String totAmt;
    /**
     * 优先股交易金额（亿元）
     */
    private String preAmt;
    /**
     * 政府债交易金额（亿元）
     */
    private String gbondAmt;
    /**
     * 信用债交易金额（亿元）
     */
    private String cbondAmt;
    /**
     * 基金交易金额（亿元）
     */
    private String fundAmt;
    /**
     * 期权交易金额（亿元）
     */
    private String optAmt;
    /**
     * 回购交易金额（亿元）
     */
    private String repAmt;
    /**
     * 股票交易金额（亿元）
     */
    private String stockAmt;

}
