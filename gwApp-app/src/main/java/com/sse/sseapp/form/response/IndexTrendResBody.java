package com.sse.sseapp.form.response;

import lombok.Data;

import java.util.List;

/**
 * 债券做市-指数走势输出对象
 *
 * @author wy
 * @date 2023-08-09
 */
@Data
public class IndexTrendResBody<T> {

    private String begin;
    private String code;
    private String date;
    private String end;
    private String highest;
    private String lowest;
    private String prev_close;
    private String time;
    private String total;
    private List<T> line;
}
