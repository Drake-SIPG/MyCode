package com.sse.sseapp.core.domain;

import lombok.Data;

/**
 * 分页参数
 *
 * @author hyin
 */
@Data
public class PageParamDto {

    /**
     * 当前页码
     */
    private int pageNum = 1;

    /**
     * 每页数量
     * 为0则查询所有
     */
    private int pageSize = 10;

}
