package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetSpecialShareholderCardStatusReqBody extends ReqContentVO {
    @NotBlank(message = "股东账号不能为空")
    @Pattern(regexp = "^[A-Z][0-9]{9}$", message = "股东账号格式不正确")
    private String partnerNum;

}
