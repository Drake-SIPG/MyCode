package com.sse.sseapp.push;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Magic. Do not touch.
 *
 * @author zhengyaosheng
 * @date   2023-03-15
 */
@Data
public class HTMessage implements Serializable {

    private static final long serialVersionUID = -58858663596147262L;
    private String[] pushIds;
    private String appId;
    private String content;
    Map< String, String> payloads;
    private String targetId;
    private String bondName;
    private Integer platform;
    private String pushId;
    private String title;
    boolean production;
    boolean showBadge;

    private long taskId;

}
