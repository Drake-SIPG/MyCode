package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/27 17:19
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetPersonDataCountReqBody extends ReqContentVO {

    private Integer userId;
    private String docId;
    private String order = "addedTime|desc";
    private String token = "APPMQUERY";
    private String stocktype;
    private String productsubtype;
    private String tradeMarket = "SH";
    private String accessToken;

    private Integer pageNo;
    private Integer pageSize;

}
