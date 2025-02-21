package com.sse.sseapp.feign.system;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 系统管理-个人信息管理-feign
 *
 * @author zhengyaosheng
 * @data 2023-02-25
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/system/profile")
public interface ISysProfileFeign {

    /**
     * 个人信息
     */
    @GetMapping("/userInfo/{username}")
    AjaxResult profile(@PathVariable("username") String username);

    /**
     * 修改用户
     */
    @PutMapping("/updateProfile")
    AjaxResult updateProfile(@RequestBody SysUser user);

    /**
     * 重置密码
     */
    @PutMapping("/updatePwd")
    AjaxResult updatePwd(@RequestParam("oldPassword") String oldPassword
                                , @RequestParam("newPassword") String newPassword
                                , @RequestParam("username") String username
                                , @RequestParam("userKey") String userKey);
}
