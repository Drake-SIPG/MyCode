package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;


@Data
public class CompanyProfileReqBody extends ReqContentVO {

    private String productId;

    private String companyCode;

    private String cpxxprodusta;

}
