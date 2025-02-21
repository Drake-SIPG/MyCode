package com.sse.sseapp.controller.office;

import com.sse.sseapp.domain.office.OpenOffice;
import com.sse.sseapp.service.office.OpenOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * openoffice
 *
 * @author wy
 * @date 2023-06-03
 */
@RestController
@RequestMapping("/dataStore/office/openoffice")
public class OpenOfficeController {

    @Autowired
    private OpenOfficeService openOfficeService;

    /**
     * 查询全部
     *
     * @return
     */
    @GetMapping("/list")
    public List<OpenOffice> selectOpenOfficeList() {
        return openOfficeService.selectOpenOfficeList();
    }

    /**
     * 新增
     *
     * @param openOffice
     * @return
     */
    @PostMapping("/insert")
    public int insert(@RequestBody OpenOffice openOffice) {
        return openOfficeService.insert(openOffice);
    }

    /**
     * 根据来源文件查询
     *
     * @param source
     * @return
     */
    @GetMapping("/source")
    public OpenOffice selectOutputBySource(@RequestParam("source") String source) {
        return openOfficeService.selectOutputBySource(source);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public int deleteById(@RequestParam("id") String id) {
        return openOfficeService.deleteById(id);
    }

}
