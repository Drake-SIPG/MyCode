package com.sse.sseapp.core.page;

import java.util.List;

/**
 * PageInfo替代。<br>
 *
 * @author hanjian
 * @date 2023/3/16 11:29 hanjian 创建
 */
public class PageInfo {
    private int pageNum;

    private int pageSize;

    private int total;

    private List<?> list;

    private int size;

    private int pages;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
