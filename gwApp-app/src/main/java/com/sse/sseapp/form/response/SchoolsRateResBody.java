package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SchoolsRateResBody {
    @JsonProperty("status")
    private String status;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("studypagename")
    private String studypagename;
    @JsonProperty("section")
    private String section;
    @JsonProperty("eifcode")
    private String eifcode;
}
