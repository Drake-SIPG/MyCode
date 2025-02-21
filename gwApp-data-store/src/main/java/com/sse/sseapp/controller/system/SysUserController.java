package com.sse.sseapp.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.sse.sseapp.core.domain.PageParamDto;
import com.sse.sseapp.core.page.GcPageInfo;
import com.sse.sseapp.domain.system.SysDept;
import com.sse.sseapp.domain.system.SysRole;
import com.sse.sseapp.domain.system.SysUser;
import com.sse.sseapp.domain.system.model.LoginUser;
import com.sse.sseapp.core.constant.UserConstants;
import com.sse.sseapp.core.domain.R;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.core.web.page.TableDataInfo;
import com.sse.sseapp.security.annotation.InnerAuth;
import com.sse.sseapp.service.system.*;
import com.sse.sseapp.utils.SecurityUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author sse
 */
@RestController
@RequestMapping("/dataStore/system/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 获取用户列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysUser user) {
        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setPageNum(user.getPageNum());
        pageParamDto.setPageSize(user.getPageSize());
        List<SysUser> list = userService.selectUserList(user);
        GcPageInfo gcPageInfo = new GcPageInfo(list, pageParamDto);
        return AjaxResult.success(gcPageInfo);
    }

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{username}")
    public R<LoginUser> info(@PathVariable("username") String username) {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("用户名或密码错误");
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }

    /**
     * 注册用户信息
     */
    @InnerAuth
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody SysUser sysUser) {
        String username = sysUser.getUserName();
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return R.fail("当前系统没有开启注册功能！");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(sysUser))) {
            return R.fail("保存用户'" + username + "'失败，注册账号已存在");
        }
        return R.ok(userService.registerUser(sysUser));
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam(value = "userId") Long userId) {
        SysUser user = userService.selectUserById(userId);
        if (user != null) {
            // 角色集合
            Set<String> roles = permissionService.getRolePermission(user);
            // 权限集合
            Set<String> permissions = permissionService.getMenuPermission(user);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("user", user);
            ajax.put("roles", roles);
            ajax.put("permissions", permissions);
            return ajax;
        }
        return AjaxResult.error("未查询到用户信息");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = { "/query/{userId}"})
    public AjaxResult query(@PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user))) {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PutMapping("/update")
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user))) {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/remove")
    public AjaxResult remove(@RequestParam("userIds") Long[] userIds, @RequestParam("userId") Long loginUserId) {
        if (ArrayUtils.contains(userIds, loginUserId)) {
            return error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        // 判断用户角色是否互斥
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        Map<Long, SysRole> roleMap = roles.stream().collect(Collectors.toMap(SysRole::getRoleId, SysRole -> SysRole));
        if (ObjectUtil.isNotEmpty(roles)) {
            List<String> roleRepelList = Arrays.asList(configService.selectConfigByKey("roleRepel").split(","));
            for (SysRole sysRole : roles) {
                if (sysRole.isFlag()) {
                    for (int i = 0; i < roleIds.length; i++) {
                        if(roleRepelList.contains(sysRole.getRoleKey() + ":" + roleMap.get(roleIds[i]).getRoleKey())
                            || roleRepelList.contains(roleMap.get(roleIds[i]).getRoleKey() + ":" + sysRole.getRoleKey())){
                            return AjaxResult.error("用户角色与授权角色互斥");
                        };
                    }
                }
            }
        }
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 获取部门树列表
     */
    @PostMapping("/deptTree")
    public AjaxResult deptTree(@RequestBody SysDept dept) {
        return success(deptService.selectDeptTreeList(dept));
    }
}
