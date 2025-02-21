package com.sse.sseapp.form.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ProductResBody extends RespContentVO {

    @JsonProperty("productCode")
    private String productCode;
    @JsonProperty("productAbbr")
    private String productAbbr;
    @JsonProperty("productFullName")
    private String productFullName;
    @JsonProperty("productType")
    private String productType;
    @JsonProperty("productSubType")
    private String productSubType;
    @JsonProperty("order")
    private String order;
    @JsonProperty("domesticIndicator")
    private String domesticIndicator;

}
