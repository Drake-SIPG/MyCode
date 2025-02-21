package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.feign.system.ISysProxyFeign;
import com.sse.sseapp.form.request.AddAppStatisticsSetupReqBody;
import com.sse.sseapp.form.request.AllCategoryDataReqBody;
import com.sse.sseapp.form.request.ParsingUrlReqBody;
import com.sse.sseapp.form.response.GetScodeResBody;
import com.sse.sseapp.interceptor.DecryptRequest;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.cominfo.dto.ParsingUrlDto;
import com.sse.sseapp.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * app统一入口
 */
@RestController
@Slf4j
public class AppController extends BaseController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    @Autowired
    CommonService commonService;

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    ISysProxyFeign sysProxyFeign;

    /**
     * 获取加密key
     */
    @PostMapping("/scode")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("获取加密key")
    public RespBean<GetScodeResBody> getScode() {
        String key = commonService.getKey(false);
        DecryptRequest.KEY.set("");
        GetScodeResBody body = new GetScodeResBody();
        body.setScode(key);
        return success(body);
    }

    /**
     * 解析URL
     */
    @PostMapping("/parsingUrl")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("解析URL")
    public RespBean<ParsingUrlDto> parsingUrl(@RequestBody BaseRequest<ParsingUrlReqBody> parsingUrlReqBody){
        return success(commonService.parsingUrl(parsingUrlReqBody));
    }

    /**
     * 获取所有类别数据
     */
    @PostMapping("/getAllCategoryData")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("获取所有类别数据")
    public RespBean<?> getAllCategoryData(@RequestBody BaseRequest<AllCategoryDataReqBody> baseRequest){
        return commonService.getAllCategoryData(baseRequest);
    }

    /**
     * 用户登录-白名单列表
     */
    @PostMapping("/whiteList")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("用户登录-白名单列表")
    public RespBean<?> whiteList(){
        return RespBean.success(commonService.whiteList());
    }

    /**
     * 获取行情前缀url
     */
    @PostMapping("/urlPrefix")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("获取行情前缀url")
    public RespBean<?> urlPrefix(){
        return RespBean.success(commonService.urlPrefix());
    }

    /**
     * APP安装量统计
     */
    @PostMapping("/addAppStatisticsSetup")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("APP安装量统计")
    public RespBean<?> addAppStatisticsSetup(@RequestBody BaseRequest<AddAppStatisticsSetupReqBody> request){
        return RespBean.success(commonService.addAppStatisticsSetup(request));
    }

    /**
     * APP安装量统计
     */
    @PostMapping("/addAppStatisticsSetupNew")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("APP安装量统计")
    public RespBean<?> addAppStatisticsSetupNew(@RequestBody BaseRequest<AddAppStatisticsSetupReqBody> request){
        return RespBean.success(commonService.addAppStatisticsSetupNew(request));
    }
}
