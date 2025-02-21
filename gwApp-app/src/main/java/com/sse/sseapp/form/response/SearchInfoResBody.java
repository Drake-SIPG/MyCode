package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import com.sse.sseapp.proxy.query.dto.SearchInfoDto;
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
public class SearchInfoResBody extends RespContentVO {
    private List<SearchInfoDto.DataDTO.KnowledgeListDTO> knowledgeList;
}
