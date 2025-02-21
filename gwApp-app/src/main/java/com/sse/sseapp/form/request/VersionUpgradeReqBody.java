package com.sse.sseapp.form.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class VersionUpgradeReqBody {

    private String appkey;
    private String version;
    private String fun = "version";

}
