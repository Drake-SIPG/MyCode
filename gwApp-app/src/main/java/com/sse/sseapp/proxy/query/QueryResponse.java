package com.sse.sseapp.proxy.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class QueryResponse<T> {
    private String success;
    private List<T> result;
    private pageHelp pageHelp;


    @Data
    private static class pageHelp{
        private Integer beginPage;
        private Integer cacheSize;
        private Integer endPage;
        private Integer pageCount;
        private Integer pageNo;
        private Integer pageSize;
        private Integer total;
    }
}
