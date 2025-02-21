package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 最新路演
 *
 * @author wy
 * @date 2023-08-24
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class RoadShowListReqBody extends ReqContentVO {

    private String type;

    private String rstype = "0";

    private Integer pageIndex;

    private Integer pageSize;

    private String companyTag = "1";

}
