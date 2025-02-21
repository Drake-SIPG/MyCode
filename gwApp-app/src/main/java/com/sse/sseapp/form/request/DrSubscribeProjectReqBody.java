package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liangjm
 * @create-date: 2023/12/28 11:16
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class DrSubscribeProjectReqBody extends ReqContentVO {
    /**
     * 用户SSO ID
     */
    private String passId;

    /**
     * 用户名（手机号）
     */
    private String phone;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 机构ID
     */
    private String intermediaryId;

}
