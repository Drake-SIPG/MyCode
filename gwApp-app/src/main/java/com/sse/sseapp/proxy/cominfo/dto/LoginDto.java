package com.sse.sseapp.proxy.cominfo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/21 15:51 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class LoginDto extends RespContentVO {
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("email")
    private String email;
    @JsonProperty("image")
    private String image;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("passId")
    private String passId;
    @JsonProperty("unionId")
    private String unionId;
    @JsonProperty("wxUnionId")
    private String wxUnionId;
}
