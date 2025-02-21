package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/26 11:18
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class RefinancingInterInfoResBody extends RespContentVO {
    /**
     * 标题
     */
    private String title;

    /**
     * 链接（相对地址，需要通过拼接过来）
     */
    private String url;

    private String domain;

    /**
     * 时间
     */
    private String date;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 通过后缀来自行判断文件类型
     */
    private String type;

    /**
     * 阅读数
     */
    private String readCount;
}
