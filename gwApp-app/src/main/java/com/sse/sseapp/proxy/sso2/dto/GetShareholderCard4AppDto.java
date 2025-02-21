package com.sse.sseapp.proxy.sso2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.sse.sseapp.app.core.domain.RespContentVO;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/22 17:33 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetShareholderCard4AppDto extends RespContentVO {
    private List<HolderCardDTO> shareholdercards = Lists.newArrayList();
    private String pass_id;
    private String wx_union_id;
    private String idCard;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class HolderCardDTO {
        private String cardnum;
        private String activestatus;
        private String cardtype;
        private String onepass;
    }
}
