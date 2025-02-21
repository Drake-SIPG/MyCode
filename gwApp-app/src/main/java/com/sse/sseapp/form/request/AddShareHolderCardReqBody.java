package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class AddShareHolderCardReqBody extends ReqContentVO {
    @NotBlank(message = "股东账号不能为空")
    @Pattern(regexp = "^[A-Z][0-9]{9}$", message = "股东账号格式不正确")
    private String partnerNum;

    @NotBlank(message = "证件类型不能为空")
    private String cardType;

    @NotBlank(message = "证件号不能为空")
    private String cardNum;

    @Pattern(regexp = "^((\\+?86)|((\\+86)))?1\\d{10}$", message = "手机号码格式不正确")
    @NotBlank(message = "手机号码不能为空")
    private String mobile;

    @NotBlank(message = "验证码不能为空")
    @JsonProperty("vCode")
    private String vcode;

}
