package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sse.sseapp.app.core.domain.RespContentVO;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/31 15:04 hanjian 创建
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetShareholderCard4AppResBody extends RespContentVO {
    private List<CardDTO> data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class CardDTO {
        private String partnerNum;

        private String active;

        private String cardtype;
    }
}
