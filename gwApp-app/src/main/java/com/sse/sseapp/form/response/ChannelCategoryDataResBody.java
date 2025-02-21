package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/31 10:11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ChannelCategoryDataResBody extends RespContentVO {
    private String categoryURL;
    private String channelFlag;
    private String categoryName;
    private String categoryOrder;
    private String menuIsCustom;
    private Integer Id;
    private Integer type;
    private String typedesc;
    private String version;

}
