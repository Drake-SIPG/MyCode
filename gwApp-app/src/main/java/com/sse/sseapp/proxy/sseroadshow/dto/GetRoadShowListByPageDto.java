package com.sse.sseapp.proxy.sseroadshow.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/13 14:36
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GetRoadShowListByPageDto<T> {

    private String count;

    private String reason;

    private List<T> roadshows;

    private Integer status;
}
