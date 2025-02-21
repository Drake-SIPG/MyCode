package com.sse.sseapp.controller;

import com.sse.sseapp.core.annotation.Log;
import com.sse.sseapp.core.enums.BusinessType;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDept;
import com.sse.sseapp.security.annotation.RequiresPermissions;
import com.sse.sseapp.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 部门信息
 *
 * @author zhengyaosheng
 * @date 2021-02-23
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
    @Autowired
    private SysDeptService deptService;

    /**
     * 获取部门列表
     */
    @RequiresPermissions("system:dept:list")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysDept dept) {
        return deptService.list(dept);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @RequiresPermissions("system:dept:list")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        return deptService.excludeChild(deptId);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @RequiresPermissions("system:dept:query")
    @GetMapping(value = "/query/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return deptService.getInfo(deptId);
    }

    /**
     * 新增部门
     */
    @RequiresPermissions("system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        return deptService.add(dept);
    }

    /**
     * 修改部门
     */
    @RequiresPermissions("system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        return deptService.edit(dept);
    }

    /**
     * 删除部门
     */
    @RequiresPermissions("system:dept:remove")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        return deptService.remove(deptId);
    }
}
