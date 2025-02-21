package com.sse.sseapp.controller;

import cn.hutool.core.util.StrUtil;
import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.ErrorMsgReqBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mateng
 * @since 2023/7/19 14:27
 */
@RestController
@RequestMapping("/errorMsg")
@Slf4j
public class ErrorMsgController extends BaseController {

    /**
     * 首页推荐接口
     */
    @PostMapping("/printErrorMsg2File")
    @Log("错误日志记录")
    @Decrypt
    @Encrypt
    public RespBean<?> printErrorMsg2File(@RequestBody BaseRequest<ErrorMsgReqBody> body) {
        ErrorMsgReqBody reqContent = body.getReqContent();
        String devType = reqContent.getDevType();
        String osType = reqContent.getOsType();
        String version = reqContent.getVersion();
        String userName = reqContent.getUserName();
        String method = reqContent.getMethod();
        String errMsg = StrUtil.toString(reqContent.getMsg());
        logger.error("=======错误信息开始===========");
        logger.error("用户:{},在设备：devType:{},osType:{},version:{},上调用方法{}出错，错误信息如下",userName,devType,osType,version,method);
        logger.error("errMsg:{}",errMsg);
        logger.error("=======错误信息结束===========");
        return RespBean.success();
    }
}
