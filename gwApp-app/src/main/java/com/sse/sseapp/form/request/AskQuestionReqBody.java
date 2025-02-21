package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AskQuestionReqBody extends ReqContentVO {

    private String source;

    private String passId;

    private String stockCode;

    private String content;

    private String method = "askQuestion";

    private String fromApp = "APPREQUEST";




}
