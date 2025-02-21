package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 路演嘉宾
 *
 * @author wy
 * @date 2023-08-24
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GuestListReqBody extends ReqContentVO {

    private Integer rsId;

    private String companyTag = "1";

}
