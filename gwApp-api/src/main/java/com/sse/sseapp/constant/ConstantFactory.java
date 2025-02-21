package com.sse.sseapp.constant;

import com.sse.sseapp.core.utils.JwtUtils;
import com.sse.sseapp.core.utils.ToolUtil;
import com.sse.sseapp.domain.system.model.LoginUserInfo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

/**
 * 工厂类-定义公共方法
 *
 * @author wangfeng
 * @date 2023/2/6 17:22
 **/
@Component
@Slf4j
public class ConstantFactory {


    /**
     * 获取登录用户信息
     *
     * @return
     */
    public LoginUserInfo getLoginUserInfo() {
        LoginUserInfo sysUserInfo = null;
        // 获取会话请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 获取token
        String token = request.getHeader("Authorization");
        if (ToolUtil.isNotEmpty(token)) {
            try {
                Claims claims = JwtUtils.parseToken(token);
                if (ToolUtil.isNotEmpty(claims)) {
                    sysUserInfo = new LoginUserInfo();
                    sysUserInfo.setUserId(claims.get("user_id", Long.class));
                    sysUserInfo.setUserKey(claims.get("user_key", String.class));
                    sysUserInfo.setUserName(claims.get("username", String.class));
                }
            } catch (Exception e) {
                log.info("获取登录用户信息失败：{}", e.getMessage());
            }
        }
        if (sysUserInfo == null) {
            sysUserInfo = new LoginUserInfo();
        }
        return sysUserInfo;
    }

    /**
     * 根据token获取用户信息
     */
    public LoginUserInfo getUserByToken(String token) {
        LoginUserInfo sysUserInfo = null;
        if (ToolUtil.isNotEmpty(token)) {
            try {
                Claims claims = JwtUtils.parseToken(token);
                if (ToolUtil.isNotEmpty(claims)) {
                    sysUserInfo = new LoginUserInfo();
                    sysUserInfo.setUserId(claims.get("user_id", Long.class));
                    sysUserInfo.setUserKey(claims.get("user_key", String.class));
                    sysUserInfo.setUserName(claims.get("username", String.class));
                }
            } catch (Exception e) {
                log.info("获取登录用户信息失败：{}", e.getMessage());
            }
        }
        if (sysUserInfo == null) {
            sysUserInfo = new LoginUserInfo();
        }
        return sysUserInfo;
    }
}
