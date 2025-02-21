package com.sse.sseapp.service.office;

import com.sse.sseapp.domain.office.OpenOffice;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * openoffice
 *
 * @author wy
 * @date 2023-06-03
 */
public interface OpenOfficeService {

    /**
     * 查询全部
     *
     * @return
     */
    @GetMapping("/list")
    List<OpenOffice> selectOpenOfficeList();

    /**
     * 新增
     *
     * @param openOffice
     * @return
     */
    int insert(OpenOffice openOffice);

    /**
     * 根据来源文件查询
     *
     * @param source
     * @return
     */
    OpenOffice selectOutputBySource(String source);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(String id);
}
