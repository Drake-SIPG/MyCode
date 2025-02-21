package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统管理-部门管理-feign
 *
 * @author zhengyaosheng
 * @data 2023-02-25
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/dept")
public interface ISysDeptFeign {

    /**
     * 获取部门列表
     */
    @PostMapping("/list")
    List<SysDept> list(@RequestBody SysDept dept);

    /**
     * 查询部门列表（排除节点）
     */
    @GetMapping("/list/exclude/{deptId}")
    List<SysDept> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId);

    /**
     * 根据部门编号获取详细信息
     */
    @GetMapping(value = "/query/{deptId}")
    SysDept getInfo(@PathVariable(value = "deptId") Long deptId);

    /**
     * 新增部门
     */
    @PostMapping("/add")
    AjaxResult add(@Validated @RequestBody SysDept dept);

    /**
     * 修改部门
     */
    @PutMapping("/edit")
    AjaxResult edit(@Validated @RequestBody SysDept dept);

    /**
     * 删除部门
     */
    @DeleteMapping("/remove/{deptId}")
    AjaxResult remove(@PathVariable(value = "deptId") Long deptId);

}
