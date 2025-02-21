package com.sse.sseapp.proxy.soa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/3/20 16:53 hanjian 创建
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SoaResponse<T> {
    private String returnCode;
    private String returnMsg;
    private Integer pageNum;
    private Integer pageSize;
    private Integer size;
    private Integer startRow;
    private Integer endRow;
    private Integer total;
    private Integer pages;
    private Integer firstPage;
    private Integer prePage;
    private Integer nextPage;
    private Integer lastPage;
    private Boolean isFirstPage;
    private Boolean isLastPage;
    private Boolean hasPreviousPage;
    private Boolean hasNextPage;
    private Integer navigatePages;
    private Integer[] navigatepageNums;
    private List<T> list;
}
