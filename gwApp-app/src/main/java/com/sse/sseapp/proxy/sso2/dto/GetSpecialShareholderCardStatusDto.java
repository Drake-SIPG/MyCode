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
public class GetSpecialShareholderCardStatusDto extends RespContentVO {
    private List<GetShareholderCard4AppDto.HolderCardDTO> shareholdercards = Lists.newArrayList();
}
