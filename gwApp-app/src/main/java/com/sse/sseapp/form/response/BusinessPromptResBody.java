package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class BusinessPromptResBody extends RespContentVO {
    /**
     * 票面利率(%)
     */
    @JsonProperty("FACE_RATE")
    private String FACE_RATE;
    /**
     * 债券代码
     */
    @JsonProperty("BOND_CODE")
    private String BOND_CODE;
    /**
     * 业务事项
     */
    @JsonProperty("ACTION")
    private String ACTION;
    /**
     * 发行量(亿元)
     */
    @JsonProperty("ISSUE_VALUE")
    private String ISSUE_VALUE;
    /**
     * 债券类型
     */
    @JsonProperty("BOND_TYPE")
    private String BOND_TYPE;
    /**
     * 债券简称
     */
    @JsonProperty("BOND_ABBR")
    private String BOND_ABBR;
    /**
     * 序号
     */
    @JsonProperty("NUM")
    private String NUM;
    /**
     * 业务日期
     */
    @JsonProperty("START_DATE")
    private String START_DATE;
    /**
     * 债券评级
     */
    @JsonProperty("BOND_RATING")
    private String BOND_RATING;
}
