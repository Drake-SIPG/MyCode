package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/23 15:35
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class OptionSchoolReqBody extends ReqContentVO {

    private String url;

    private String sendType;

    private Map<String,Object> params;

}
