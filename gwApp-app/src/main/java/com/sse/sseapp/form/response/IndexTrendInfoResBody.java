package com.sse.sseapp.form.response;

import lombok.Data;

import java.util.List;

/**
 * 债券做市-指数走势概况输出对象
 *
 * @author wy
 * @date 2023-08-14
 */
@Data
public class IndexTrendInfoResBody<T> {
    private List<T> snap;
}
