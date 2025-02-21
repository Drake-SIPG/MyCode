package com.sse.sseapp.controller.system;

import com.sse.sseapp.core.domain.PageParamDto;
import com.sse.sseapp.core.page.GcPageInfo;
import com.sse.sseapp.domain.system.SysDictType;
import com.sse.sseapp.core.constant.UserConstants;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.core.web.page.TableDataInfo;
import com.sse.sseapp.service.system.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典类型
 *
 * @author zhengyaosheng
 * @date 2023-03-01
 */
@RestController
@RequestMapping("/dataStore/system/dict/type")
public class SysDictTypeController extends BaseController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysDictType dictType) {
        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setPageNum(dictType.getPageNum());
        pageParamDto.setPageSize(dictType.getPageSize());
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        GcPageInfo gcPageInfo = new GcPageInfo(list, pageParamDto);
        return AjaxResult.success(gcPageInfo);
    }

    /**
     * 查询字典类型详细
     */
    @GetMapping(value = "/query/{dictId}")
    public AjaxResult getInfo(@PathVariable Long dictId) {
        return success(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return toAjax(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return toAjax(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/remove/{dictIds}")
    public AjaxResult remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }

    /**
     * 刷新字典缓存
     */
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        dictTypeService.resetDictCache();
        return success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionSelect")
    public AjaxResult optionSelect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return success(dictTypes);
    }

    @GetMapping("/getInfoByType/{dictType}")
    public SysDictType getInfoByType(@PathVariable(value = "dictType") String dictType){
        return dictTypeService.getInfoByType(dictType);
    }
}
