package com.sse.sseapp.proxy.soa.dto;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/13 9:40
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ShareholdersMeetingListDto extends RespContentVO {

    /**
     * CalendarRes      返回数据中会返回路演和日历的数据
     */
    List<ShareholdersMeetingListRes> list;

    /**
     * 标题
     */
    private String title;

    /**
     * 总数
     */
    private String total;

    /**
     * 返回数据类型   4为股东大会  6为路演
     */
    private String type;

    @Data
    public static class ShareholdersMeetingListRes {

        public String bizSeq;

        /**
         * 返回数据类型   4为股东大会  2为路演
         */
        public String bizType;

        public String bizTypeDesc;

        public String code;

        public String name;

        public String rsurl;

        public String subtype;

        public String title;

        public String tradeBeginDate;

        public String tradeEndDate;

        public String type;

        public String updateTime;
    }

}
