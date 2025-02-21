package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 债券做市-做市场输出对象
 *
 * @author wy
 * @date 2023-08-07
 */
@Data
public class MarketMakerResBody {

    /**
     * 证券品种
     * GP：股票
     * JJ：基金
     * ZQ：债券
     * QQ：期权
     * DR：存托凭证
     */
    @JsonProperty("PRODUCT_TYPE")
    private String productType;

    /**
     * 做市商名称
     */
    @JsonProperty("FIRM_NAME")
    private String firmName;

    /**
     * 做市机构代码
     */
    @JsonProperty("FIRM_CODE")
    private String firmCode;

    /**
     * 做市类型
     * 0：主做市商
     * 1：一般做市商
     */
    @JsonProperty("ZS_TYPE")
    private String zsType;

    /**
     * 机构类型（该字段只有证券品种为基金时取值，其余证券品种该字段为空）
     */
    @JsonProperty("FIRM_TYPE")
    private String firmType;

    /**
     * 具有业务资格
     * ZS：做市
     * KZ：跨境转换
     */
    @JsonProperty("QUALIFY_TYPE")
    private String qualifyType;

    /**
     * 文件日期（YYYYMMDD）
     */
    @JsonProperty("FILE_DATE")
    private String fileDate;
}
