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
public class StockBoardTypeResBody extends RespContentVO {

    @JsonProperty("type")
    private String type;
    @JsonProperty("companyCode")
    private String companyCode;
    @JsonProperty("typeDesc")
    private String typeDesc;

}
