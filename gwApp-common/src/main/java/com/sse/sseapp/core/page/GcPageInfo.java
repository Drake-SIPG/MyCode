package com.sse.sseapp.core.page;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.PageUtil;
import com.sse.sseapp.core.domain.PageParamDto;
import java.util.List;
import lombok.Data;

/**
 * 逻辑分页
 *
 * @author hyin
 */
@Data
public class GcPageInfo extends PageInfo {

    public GcPageInfo(List rowData, PageParamDto pageParamDto) {
        this.pagination(rowData, pageParamDto);
    }

    /**
     * 逻辑分页
     *
     * @param rowData
     * @param pageParamDto
     */
    private void pagination(List rowData, PageParamDto pageParamDto) {
        // 页码
        int pageNum = pageParamDto.getPageNum();
        // 每页条目数
        int pageSize = pageParamDto.getPageSize();
        // 总条目数
        int total = rowData.size();
        // 总页数
        int totalPage = PageUtil.totalPage(total, pageSize);
        // 索引
        int index = pageNum - 1 > 0 ? pageNum - 1 : 0;
        int[] startEnd = PageUtil.transToStartEnd(index, pageSize);
        // 截取数据
        List sub = CollectionUtil.sub(rowData, startEnd[0], startEnd[1]);
        // 当前数据条目
        int size = sub.size();

        this.setPageNum(pageNum);
        this.setPageSize(pageSize);
        this.setTotal(total);
        this.setPages(totalPage);
        this.setList(sub);
        this.setSize(size);
    }

}
