package com.sse.sseapp.controller.system;

import com.sse.sseapp.core.utils.ToolUtil;
import com.sse.sseapp.domain.system.SysUser;
import com.sse.sseapp.domain.system.model.LoginUser;
import com.sse.sseapp.core.constant.UserConstants;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.redis.service.RedisService;
import com.sse.sseapp.service.TokenService;
import com.sse.sseapp.utils.SecurityUtils;
import com.sse.sseapp.service.system.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 个人信息 业务处理
 *
 * @author sse
 */
@RestController
@RequestMapping("/dataStore/system/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;


    /**
     * 个人信息
     */
    @GetMapping("/userInfo/{username}")
    public AjaxResult profile(@PathVariable("username") String username) {
        SysUser user = userService.selectUserByUserName(username);
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(username));
        return ajax;
    }

    /**
     * 修改用户
     */
    @PutMapping("updateProfile")
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = null;
        if (ToolUtil.isNotEmpty(user.getUserKey())) {
            loginUser = redisService.getCacheObject(TokenService.getTokenKey(user.getUserKey()));
            SysUser sysUser = loginUser.getSysUser();
            user.setUserName(sysUser.getUserName());
            if (StringUtils.isNotEmpty(user.getPhonenumber())
                    && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
                return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
            } else if (StringUtils.isNotEmpty(user.getEmail())
                    && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
                return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
            }
            user.setUserId(sysUser.getUserId());
            user.setPassword(null);
            user.setAvatar(null);
            user.setDeptId(null);
            if (userService.updateUserProfile(user) > 0) {
                // 更新缓存用户信息
                loginUser.getSysUser().setNickName(user.getNickName());
                loginUser.getSysUser().setPhonenumber(user.getPhonenumber());
                loginUser.getSysUser().setEmail(user.getEmail());
                loginUser.getSysUser().setSex(user.getSex());
                tokenService.setLoginUser(loginUser);
                return success();
            }
        }

        return error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword, String username, String userKey) {
        SysUser user = userService.selectUserByUserName(username);
        String password = user.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return error("新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(username, SecurityUtils.encryptPassword(newPassword)) > 0) {
            // 更新缓存用户密码
            LoginUser loginUser = redisService.getCacheObject(TokenService.getTokenKey(userKey));
            loginUser.getSysUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }


}
