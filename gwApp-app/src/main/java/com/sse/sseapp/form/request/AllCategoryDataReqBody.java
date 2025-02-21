package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class AllCategoryDataReqBody extends ReqContentVO {

    private Integer id;
    private String version;
    private String categoryName;
    private String categoryOrder;
    private String type;
    private String channelFlag;
    private String order;



//    Id	int	否		id
//    version	String 	否		系统版本号:如1.1.2，用于查询小于或者等于该版本的最近一个版本的数据
//    categoryName	string	否		分类名称/股票名称/专题名称
//    categoryOrder	string	否		显示顺序
//    type	int	否		类型：一级栏目：1，公告类别：2，监管类别，3，行情列表页面股票名称，4，专题首页分类
//    channelFlag	string	否		用于app记录栏目标识
//    order	string	否	categoryOrder|asc	组合排序条件，多个排序条件用逗号分隔，排序字段和排序类型用竖划线’|’分隔，排在前面的字段排序级别越高。排序字段为：categoryOrder、type、channelFlag排序类型为：desc、asc。
}
