package com.sse.sseapp.form.response.quotes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/7/5 14:17
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Month {
    /**
     * date : 20230406
     * time : 151741
     * total : 12
     * begin : 0
     * end : 12
     * list : [["510050",202304],["510050",202305],["510050",202306],["510050",202309],["510300",202304],["510300",202305],["510300",202306],["510300",202309],["510500",202304],["510500",202305],["510500",202306],["510500",202309]]
     */
    @JsonProperty("date")
    private Integer date;
    @JsonProperty("time")
    private Integer time;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("begin")
    private Integer begin;
    @JsonProperty("end")
    private Integer end;
    @JsonProperty("list")
    private List<List<String>> list;
}
