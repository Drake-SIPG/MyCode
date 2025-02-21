package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class CalendarDayListReqBody extends ReqContentVO {

    private String token="APPMQUERY";
    private String page="1";
    private String pageSize="100";
    private String tradeBeginDate;
    private String dateType = "all";
    private String tradeEndDate;
    private String order="tday|desc";

//    tradeBeginDate	string	是		开始日期YYYYMMDD
//    tradeEndDate	string	是		结束日期YYYYMMDD
//    dateType	string	否	all	y：查询结果中返回只包含交易日期的数据
//    n：查询结果中返回只包含非交易日的数据
//    nh：查询结果中返回除周六、周日外的非交易日数据
//    all：查询结果返回区间段的所有交易日期情况
//    order	string	否	tday|desc	排序字段与排序类型间用‘|’分隔，排序字段为：tday；排序类型为：desc 、asc。
}
