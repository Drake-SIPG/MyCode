package com.sse.sseapp.form.request;


import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class MarketMainSharesReqBody extends ReqContentVO {

    private String productCode;
    private String excludeProductCode;
    private String productAbbr;
    private String productFullName;
    private String productType = "EQU,FUN,BON";
    private String productSubType;
    private String order = "productCode";
    private Integer pageNo;


//    productCode	string	否		产品代码，支持按代码模糊查找
//    excludeProductCode	string	否		不含的产品代码，支持集合操作.多个代码逗号分隔。
//    productAbbr	string	否		产品简称，支持模糊
//    productFullName	string	否		产品全称，支持模糊
//    productType	string	否		EQU为股票
//    BON为债券
//            FUN为基金
//    WAR为权证
//            FUT为期货
//，支持集合操作，详情见产品类型与子类型的描述表
//    productSubType	string	否		支持集合操作，见产品类型与子类型的描述表
//    order	string	否	productCode |desc	组合排序条件，多个排序条件用逗号分隔，排序字段和排序类型用竖划线’|’分隔，排在前面的字段排序级别越高。排序字段为：productCode、productType、productSubType排序类型为：desc 、asc。
//

}
