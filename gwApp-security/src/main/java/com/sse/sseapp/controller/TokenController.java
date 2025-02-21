package com.sse.sseapp.controller;


import com.sse.sseapp.constant.ConstantFactory;
import com.sse.sseapp.core.domain.R;
import com.sse.sseapp.core.utils.Base64;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.domain.system.model.LoginUser;
import com.sse.sseapp.form.LoginBody;
import com.sse.sseapp.form.RegisterBody;
import com.sse.sseapp.service.SysLoginService;
import com.sse.sseapp.service.TokenService;
import com.sse.sseapp.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * token 控制
 *
 * @author sse
 */
@RestController
@RequestMapping("/auth")
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private ConstantFactory constantFactory;

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginBody form) {
        // 用户登录 密码进行Base64解码
        LoginUser userInfo = sysLoginService.login(Base64.decode(form.getUsername()), Base64.decode(form.getPassword()));
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    @DeleteMapping("/logout")
    public R<?> logout(@RequestParam("token") String token) {
        if (StringUtils.isNotEmpty(token)) {
            String username = constantFactory.getUserByToken(token).getUserName();
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(@RequestParam("token") String token) {
        LoginUser loginUser = tokenService.getLoginUser(token);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody RegisterBody registerBody) {
        // 用户注册
        sysLoginService.register(registerBody.getUsername(), registerBody.getPassword());
        return R.ok();
    }
}
