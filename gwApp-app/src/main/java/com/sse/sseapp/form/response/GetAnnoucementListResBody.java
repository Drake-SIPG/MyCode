package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/14 10:25
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetAnnoucementListResBody extends RespContentVO {
    private String docTitle;
    private String stockcode;
    private String docSize;
    private String docId;
    private String docType;
    private String createTime;
    private String docURL;
}
