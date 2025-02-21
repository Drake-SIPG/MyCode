package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 用户登录注册以及登出
 */
@RestController
@RequestMapping("/oauth")
@Slf4j
public class OauthController extends BaseController {

    @Autowired
    OauthService oauthService;

    /**
     * app加密入口
     */
    @PostMapping("/login")
    @ResponseBody
    @Log("用户名密码登录")
    @Decrypt
    @Encrypt
    public RespBean login(@RequestBody BaseRequest<LoginReqBody> loginReqBody) {
        return success(oauthService.login(loginReqBody).getData());
    }

    /**
     * 信创app加密入口
     */
    @PostMapping("/loginNew")
    @ResponseBody
    @Log("信创用户名密码登录")
    @Decrypt
    @Encrypt
    public RespBean loginNew(@RequestBody BaseRequest<LoginReqBody> loginReqBody) {
        return success(oauthService.loginNew(loginReqBody).getData());
    }

    /**
     *  接口用于短信登录
     */
    @PostMapping("/smslogin")
    @ResponseBody
    @Log("短信登录")
    @Decrypt
    @Encrypt
    public RespBean smsLogin(@RequestBody BaseRequest<SmsLoginReqBody> smsLoginReqBody){
        return success(oauthService.smsLogin(smsLoginReqBody).getData());
    }

    /**
     *  信创短信登录
     */
    @PostMapping("/smsloginNew")
    @ResponseBody
    @Log("信创短信登录")
    @Decrypt
    @Encrypt
    public RespBean smsLogiNew(@RequestBody BaseRequest<SmsLoginReqBody> smsLoginReqBody){
        return success(oauthService.smsLoginNew(smsLoginReqBody).getData());
    }

    /**
     * 接口用于忘记密码
     */
    @PostMapping("/forgotpassword")
    @ResponseBody
    @Log("忘记密码")
    @Decrypt
    @Encrypt
    public RespBean forgotPassword(@RequestBody BaseRequest<ForgotPasswordReqBody> forgotPasswordReqBody){
        return success(oauthService.forgotPassword(forgotPasswordReqBody).getData());
    }

    /**
     * 信创忘记密码
     */
    @PostMapping("/forgotpasswordNew")
    @ResponseBody
    @Log("信创忘记密码")
    @Decrypt
    @Encrypt
    public RespBean forgotPasswordNew(@RequestBody BaseRequest<ForgotPasswordReqBody> forgotPasswordReqBody){
        return success(oauthService.forgotPasswordNew(forgotPasswordReqBody).getData());
    }

    /**
     * 接口用于注册
     */
    @PostMapping("/register")
    @ResponseBody
    @Log("注册")
    @Decrypt
    @Encrypt
    public RespBean register(@RequestBody BaseRequest<RegisterReqBody> registerReqBody){
        return success(oauthService.register(registerReqBody).getData());
    }

    /**
     * 信创注册
     */
    @PostMapping("/registerNew")
    @ResponseBody
    @Log("注册")
    @Decrypt
    @Encrypt
    public RespBean registerNew(@RequestBody BaseRequest<RegisterReqBody> registerReqBody){
        return success(oauthService.registerNew(registerReqBody).getData());
    }

    /**
     * 接口用于登出
     */
    @PostMapping("/loginOut")
    @ResponseBody
    @Log("登出")
    @Decrypt
    @Encrypt
    public RespBean loginOut(@RequestBody BaseRequest<LoginOutReqBody> loginOutReqBody){
        return success(oauthService.loginOut(loginOutReqBody).getData());
    }

    /**
     * 接口用于登出
     */
    @PostMapping("/loginOutNew")
    @ResponseBody
    @Log("登出")
    @Decrypt
    @Encrypt
    public RespBean loginOutNew(@RequestBody BaseRequest<LoginOutReqBody> loginOutReqBody){
        return success(oauthService.loginOutNew(loginOutReqBody).getData());
    }

    /**
     * 接口用于获取短信验证码
     */
    @PostMapping("/smscaptcha")
    @ResponseBody
    @Log("获取短信验证码")
    @Decrypt
    @Encrypt
    public RespBean smsCaptcha(@RequestBody BaseRequest<SmsCaptchaReqBody> smsCaptchaReqBody){
        return success(oauthService.smsCaptcha(smsCaptchaReqBody).getData());
    }

    /**
     * 接口用于获取短信验证码
     */
    @PostMapping("/smscaptchaNew")
    @ResponseBody
    @Log("信创获取短信验证码")
    @Decrypt
    @Encrypt
    public RespBean smsCaptchaNew(@RequestBody BaseRequest<SmsCaptchaReqBody> smsCaptchaReqBody){
        return success(oauthService.smsCaptchaNew(smsCaptchaReqBody).getData());
    }

    /**
     * 接口用于获取图形验证码
     */
    @PostMapping("/piccaptcha")
    @ResponseBody
    @Log("获取图形验证码")
    @Decrypt
    @Encrypt
    public RespBean picCaptcha(@RequestBody BaseRequest<PicCaptchaReqBody> picCaptchaReqBody){
        return success(oauthService.picCaptcha(picCaptchaReqBody).getData());
    }

    /**
     * 接口用于获取图形验证码
     */
    @PostMapping("/piccaptchaNew")
    @ResponseBody
    @Log("信创获取图形验证码")
    @Decrypt
    @Encrypt
    public RespBean picCaptchaNew(@RequestBody BaseRequest<PicCaptchaReqBody> picCaptchaReqBody){
        return success(oauthService.picCaptchaNew(picCaptchaReqBody).getData());
    }

    /**
     * 接口用于二维码扫描登录
     */
    @PostMapping("/scanLogin")
    @ResponseBody
    @Log("二维码扫码登录")
    @Decrypt
    @Encrypt
    public RespBean scanLogin(@RequestBody BaseRequest<ScanLoginReqBody> scanLoginReqBody){
        return success(oauthService.scanLogin(scanLoginReqBody).getData());
    }

    /**
     * 信创二维码扫描登录
     */
    @PostMapping("/scanLoginNew")
    @ResponseBody
    @Log("信创二维码扫码登录")
    @Decrypt
    @Encrypt
    public RespBean scanLoginNew(@RequestBody BaseRequest<ScanLoginReqBody> scanLoginReqBody){
        return success(oauthService.scanLoginNew(scanLoginReqBody).getData());
    }

    /**
     * 信创获取scancode
     */
    @PostMapping("/getScanCode")
    @ResponseBody
    @Log("信创获取scancode")
    @Decrypt
    @Encrypt
    public RespBean getScanCode(@RequestBody BaseRequest<GetScanCodeReqBody> getScanCodeReqBody){
        return success(oauthService.getScanCode(getScanCodeReqBody).getData());
    }

    /**
     * 信创获取clientID
     */
    @PostMapping("/getClientId")
    @ResponseBody
    @Log("信创获取clientId")
    @Decrypt
    @Encrypt
    public RespBean getClientId(@RequestBody BaseRequest<GetScanCodeReqBody> getScanCodeReqBody){
        return success(oauthService.getClientId(getScanCodeReqBody).getData());
    }

    /**
     * 接口用于查看头像
     */
    @PostMapping("/getUserImage")
    @ResponseBody
    @Log("查看头像")
    @Decrypt
    @Encrypt
    public RespBean getUserImage(@RequestBody BaseRequest<GetUserImageReqBody> getUserImageReqBody){
        return RespBean.success(oauthService.getUserImage(getUserImageReqBody).getData());
    }

    /**
     * 接口用于查看头像
     */
    @PostMapping("/getUserImageNew")
    @ResponseBody
    @Log("信创查看头像")
    @Decrypt
    @Encrypt
    public RespBean getUserImageNew(@RequestBody BaseRequest<GetUserImageReqBody> getUserImageReqBody){
        return RespBean.success(oauthService.getUserImageNew(getUserImageReqBody).getData());
    }

    /**
     * 接口用于刷新token
     */
    @PostMapping("/refreshToken")
    @ResponseBody
    @Log("刷新token")
    @Decrypt
    @Encrypt
    public RespBean refreshToken(@RequestBody BaseRequest<RefreshTokenReqBody> refreshTokenReqBody){
        return RespBean.success(oauthService.refreshToken(refreshTokenReqBody).getData());
    }

    /**
     * 接口用于刷新token
     */
    @PostMapping("/refreshTokenNew")
    @ResponseBody
    @Log("信创刷新token")
    @Decrypt
    @Encrypt
    public RespBean refreshTokenNew(@RequestBody BaseRequest<RefreshTokenReqBody> refreshTokenReqBody){
        return RespBean.success(oauthService.refreshTokenNew(refreshTokenReqBody).getData());
    }

    /**
     * 一键登录
     */
    @PostMapping("/oneClickLogin")
    @ResponseBody
    @Log("移动一键登录")
    @Decrypt
    @Encrypt
    public RespBean oneClickLogin(@RequestBody BaseRequest<OneClickLoginReqBody>  oneClickLoginReqBody){
        return RespBean.success(oauthService.oneClickLogin(oneClickLoginReqBody).getData());
    }

}
