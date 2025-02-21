package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.AppDrProjectDynamic;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/drDynamic")
public interface ISysAppDrProjectDynamicFeign {

    @GetMapping("/list")
    public AjaxResult getList();

    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody AppDrProjectDynamic appDrProjectDynamic);

    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody AppDrProjectDynamic appDrProjectDynamic);

    @DeleteMapping("/remove/{roleIds}")
    public AjaxResult remove(@PathVariable(value = "roleIds") String[] roleIds);

    @GetMapping("/getAllList")
    List<AppDrProjectDynamic> getAllList();

}
