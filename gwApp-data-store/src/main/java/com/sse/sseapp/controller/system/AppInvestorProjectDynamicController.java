package com.sse.sseapp.controller.system;


import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.AppInvestorProjectDynamic;
import com.sse.sseapp.service.system.AppInvestorProjectDynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 投资者项目动态 前端控制器
 * </p>
 *
 * @author liuxinyu
 * @since 2023-05-31
 */
@RestController
@RequestMapping("/dataStore/system/dynamic")
public class AppInvestorProjectDynamicController extends BaseController {
    @Autowired
    AppInvestorProjectDynamicService service;

    @GetMapping("/list")
    public AjaxResult getList(){
        return success(service.list());
    }

    /**
     * 修改保存角色
     */
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody AppInvestorProjectDynamic appInvestorProjectDynamic){
        return success(service.saveOrUpdate(appInvestorProjectDynamic));
    }

    /**
     * 修改保存角色
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody AppInvestorProjectDynamic appInvestorProjectDynamic){
        return success(service.saveOrUpdate(appInvestorProjectDynamic));
    }

    @DeleteMapping("/remove/{roleIds}")
    public AjaxResult remove(@PathVariable("roleIds") String[] roleIds){
        return success(service.removeById(roleIds));
    }

    @GetMapping("/getAllList")
    public List<AppInvestorProjectDynamic> getAllList(){
        return service.list();
    }

}
