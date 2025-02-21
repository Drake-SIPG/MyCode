package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class MarginTradingNoticeResBody extends RespContentVO {
    private String createTime;
    private String docId;
    private String docType;
    private String docURL;
    private String siteId;
    private String docSize;
    private String docTitle;
    private String channelId;
    private String docKeyword;

}
