package com.sse.sseapp.form.request;

import lombok.Data;

@Data
public class VersionUpgradeNewReqBody {

    private String appBundle;
    private String version;
    private String fun = "version";
    private String appType;

}
