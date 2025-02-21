package com.sse.sseapp.controller;

import com.sse.sseapp.core.annotation.Log;
import com.sse.sseapp.core.enums.BusinessType;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysUser;
import com.sse.sseapp.service.SysProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 个人信息
 *
 * @author zhengyaosheng
 * @date 2021-02-23
 */
@RestController
@RequestMapping("/system/profile")
public class SysProfileController {

    @Autowired
    private SysProfileService sysProfileService;

    /**
     * 个人信息
     */
    @GetMapping("/userInfo")
    public AjaxResult profile() {
        return sysProfileService.profile();
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updateProfile")
    public AjaxResult updateProfile(@RequestBody SysUser user){
        return sysProfileService.edit(user);
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        return sysProfileService.updatePwd(oldPassword, newPassword);
    }




}
