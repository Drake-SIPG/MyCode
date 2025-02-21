package com.sse.sseapp.service;

import com.sse.sseapp.constant.ConstantFactory;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysUser;
import com.sse.sseapp.feign.system.ISysProfileFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 系统管理-个人信息管理 service
 *
 * @author zhengyaosheng
 * @date 2023/02/24
 **/
@Slf4j
@Service
public class SysProfileService {

    @Autowired
    private ISysProfileFeign sysProfileFeign;

    @Autowired
    private ConstantFactory constantFactory;

    /**
     * 个人信息
     */
    public AjaxResult profile() {
        String username = constantFactory.getLoginUserInfo().getUserName();
        return this.sysProfileFeign.profile(username);
    }

    /**
     * 修改用户
     */
    public AjaxResult edit(SysUser user) {
        String userKey = constantFactory.getLoginUserInfo().getUserKey();
        user.setUserKey(userKey);
        return this.sysProfileFeign.updateProfile(user);
    }

    /**
     * 重置密码
     */
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        String userKey = constantFactory.getLoginUserInfo().getUserKey();
        return this.sysProfileFeign.updatePwd(oldPassword, newPassword, username, userKey);
    }


}
