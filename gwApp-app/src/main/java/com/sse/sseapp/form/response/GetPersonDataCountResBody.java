package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/27 17:54
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetPersonDataCountResBody extends RespContentVO {

    /**
     * 我的收藏总数
     */
    private Integer favouriteCount;

    /**
     * 我的自选股总数
     */
    private Integer optionalStockCount;

    /**
     * 我的消息总数
     */
    private Integer messageCount;

}
