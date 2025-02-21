package com.sse.sseapp.proxy.einteract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/12 14:27
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EInteractResponse<T> {

    private Integer count;

    private String reason;

    private Integer status;

    /**
     * 不知道什么意义的参数，但是会返回回来
     */
    private Boolean state;

    private String domain_base;

    private String domain_res;

    private List<T> qas;


}
