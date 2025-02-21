package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import com.sse.sseapp.service.SearchService;
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
public class SearchDataResBody extends RespContentVO {
    @JsonProperty("answer_list")
    private List<SearchService.SearchDataResDto.AnswerListDTO> answerList;
    @JsonProperty("matchedList")
    private List<SearchService.SearchDataResDto.MatchedListDTO> matchedList;
}
