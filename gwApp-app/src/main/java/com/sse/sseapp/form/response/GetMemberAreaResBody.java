package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/24 16:17
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetMemberAreaResBody extends RespContentVO {
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
     * 营业部数
     */
    private String branchNum;

    /**
     * A股席位
     */
    private String aSeats;

    /**
     * B股席位
     */
    private String bSeats;

    /**
     * 合计
     */
    private String totSeats ;

    /**
     * 地区：含“其他”及“合计”
     * AREA 是 '其他'：2, '合计'：3, 其余返回1
     */
    private String areaOrder;


    @JsonGetter("aSeats")
    public String getaSeats() {
        return aSeats;
    }

    @JsonGetter("bSeats")
    public String getbSeats() {
        return bSeats;
    }
}
