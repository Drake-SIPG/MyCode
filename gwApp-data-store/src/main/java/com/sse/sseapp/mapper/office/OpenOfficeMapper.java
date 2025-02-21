package com.sse.sseapp.mapper.office;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.office.OpenOffice;

import java.util.List;

/**
 * openoffice
 *
 * @author wy
 * @date 2023-06-03
 */
public interface OpenOfficeMapper extends BaseMapper<OpenOffice> {

    /**
     * 根据来源文件查询输出文件目录
     *
     * @param source
     * @return
     */
    OpenOffice selectOutputBySource(String source);

    /**
     * 新增
     *
     * @param openOffice
     * @return
     */
    int insertOpenOffice(OpenOffice openOffice);
}
