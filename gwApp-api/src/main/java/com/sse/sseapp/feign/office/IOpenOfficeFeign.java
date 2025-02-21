package com.sse.sseapp.feign.office;

import com.sse.sseapp.domain.office.OpenOffice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * openoffice-feign
 *
 * @author wy
 * @data 2023-06-03
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/office/openoffice")
public interface IOpenOfficeFeign {

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
    @PostMapping("/insert")
    int insert(@RequestBody OpenOffice openOffice);

    /**
     * 根据来源文件查询
     *
     * @param source
     * @return
     */
    @GetMapping("/source")
    OpenOffice selectOutputBySource(@RequestParam("source") String source);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteById")
    int deleteById(@RequestParam("id") String id);
}
