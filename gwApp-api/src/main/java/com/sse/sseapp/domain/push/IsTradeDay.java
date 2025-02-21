package com.sse.sseapp.domain.push;

import lombok.Data;

import java.util.List;

/**
 * 是否是交易日
 *
 * @author wy
 * @date 2023-09-20
 */
@Data
public class IsTradeDay {

    private static final long serialVersionUID = 1L;

    private List<ListDTO> list;

    @Data
    public static class ListDTO {
        private String isTradeDay;
        private String tday;
    }
}
