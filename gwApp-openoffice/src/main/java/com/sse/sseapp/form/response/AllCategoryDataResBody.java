package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AllCategoryDataResBody {

    @JsonProperty("categoryName")
    private String categoryName;
    @JsonProperty("categoryOrder")
    private String categoryOrder;
    @JsonProperty("categoryURL")
    private String categoryURL;
    @JsonProperty("channelFlag")
    private String channelFlag;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("type")
    private Integer type;
    @JsonProperty("typedesc")
    private String typedesc;
    @JsonProperty("version")
    private String version;
    @JsonProperty("isCustom")
    private String isCustom;

}
