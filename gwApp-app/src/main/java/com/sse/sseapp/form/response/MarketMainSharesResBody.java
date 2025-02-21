package com.sse.sseapp.form.response;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class MarketMainSharesResBody extends RespContentVO {

    private String code;
    private String name;
    @JsonProperty("productFullName")
    private String productFullName;
    @JsonProperty("productType")
    private String productType;
    @JsonProperty("productSubType")
    private String productSubType;
    @JsonProperty("order")
    private String order;

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

    //    <productCode clientmapping="code"></productCode>
//				<productAbbr clientmapping="name"></productAbbr>
//				<productFullName></productFullName>
//				<productType></productType>
//				<productSubType></productSubType>
//				<order></order>
}
