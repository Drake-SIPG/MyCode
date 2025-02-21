package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MarketMainSharesListResBody extends RespContentVO {
    /**
     * 产品代码
     */
    private String code;

    /**
     * 官方发行时间
     */
    @JsonProperty("instrumentIssueDate")
    private Date instrumentIssueDate;

    /**
     * 产品全称
     */
    private String name;

    @JsonProperty("productFullName")
    private String productFullName;

    /**
     * 产品子类型
     */
    @JsonProperty("productSubType")
    private String productSubType;

    /**
     * 产品类型
     */
    @JsonProperty("productType")
    private String productType;

    @JsonGetter("code")
    public String getCode() {
        return code;
    }

    @JsonSetter("productCode")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonSetter("productAbbr")
    public void setName(String name) {
        this.name = name;
    }
}
