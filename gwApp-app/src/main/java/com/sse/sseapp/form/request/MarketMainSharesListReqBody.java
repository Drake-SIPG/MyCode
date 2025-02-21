package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

@Data
public class MarketMainSharesListReqBody extends ReqContentVO {
    /**
     * 搜索关键字
     */
    private String productCode;

    /**
     * 产品缩写
     */
    private String productAbbr;
    /**
     * 页码
     */
    private Integer pageNo = 1;

    /**
     * 	展示数据多少
     */
    private Integer pageSize = 20;

    /**
     * 排序
     */
    private String order = "productCode";

    /**
     * 产品类型
     */
    private String productType = "BON,FUN,EQU";

}
