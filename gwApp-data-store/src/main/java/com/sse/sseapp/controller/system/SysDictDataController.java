package com.sse.sseapp.controller.system;

import com.sse.sseapp.core.domain.PageParamDto;
import com.sse.sseapp.core.page.GcPageInfo;
import com.sse.sseapp.domain.system.SysDictData;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.core.web.page.TableDataInfo;
import com.sse.sseapp.service.system.ISysDictDataService;
import com.sse.sseapp.service.system.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据字典信息
 *
 * @author sse
 */
@RestController
@RequestMapping("/dataStore/system/dict/data")
public class SysDictDataController extends BaseController {
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysDictData dictData) {
        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setPageNum(dictData.getPageNum());
        pageParamDto.setPageSize(dictData.getPageSize());
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        GcPageInfo gcPageInfo = new GcPageInfo(list, pageParamDto);
        return AjaxResult.success(gcPageInfo);
    }

    /**
     * 查询字典数据详细
     */
    @GetMapping(value = "/query/{dictCode}")
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        return success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return success(data);
    }

    /**
     * 新增字典类型
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysDictData dict) {
        return toAjax(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/remove/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }

    @RequestMapping("/getDictMap/{dictType}")
    public List<Map<String,Object>> getDictMap(@PathVariable String dictType){
        return dictDataService.getDictMap(dictType);
    }
}
