package com.sse.sseapp.controller.system;


import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.AppDrProjectDynamic;
import com.sse.sseapp.service.system.AppDrProjectDynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * Dr项目动态 前端控制器
 * </p>
 *
 * @author liangjm
 * @since 2024-01-23
 */
@RestController
@RequestMapping("/dataStore/system/drDynamic")
public class AppDrProjectDynamicController extends BaseController {
    @Autowired
    AppDrProjectDynamicService service;

    @GetMapping("/list")
    public AjaxResult getList(){
        return success(service.list());
    }

    /**
     * 修改保存角色
     */
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody AppDrProjectDynamic appDrProjectDynamic){
        return success(service.saveOrUpdate(appDrProjectDynamic));
    }

    /**
     * 修改保存角色
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody AppDrProjectDynamic appDrProjectDynamic){
        return success(service.saveOrUpdate(appDrProjectDynamic));
    }

    @DeleteMapping("/remove/{roleIds}")
    public AjaxResult remove(@PathVariable("roleIds") String[] roleIds){
        return success(service.removeById(roleIds));
    }

    @GetMapping("/getAllList")
    public List<AppDrProjectDynamic> getAllList(){
        return service.list();
    }

}
