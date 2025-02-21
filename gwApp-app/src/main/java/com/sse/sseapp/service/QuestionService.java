package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.utils.Util;
import com.sse.sseapp.form.request.AskQuestionReqBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.einteract.dto.AskQuestionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : liuxinyu
 * @date : 2023/4/3 17:41
 */
@Service
public class QuestionService {

    @Autowired
    HttpServletRequest request;

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    CommonService commonService;

    public AskQuestionDto askQuestion(BaseRequest<AskQuestionReqBody> askQuestionReq) {
        //check user判断用户是否登录
        commonService.cominfoCheck(askQuestionReq.getBase());

        Map<String, Object> data = BeanUtil.beanToMap(askQuestionReq.getReqContent());

        //初始化data
        data.put("requestIP", askQuestionReq.getBase().getIp());
        //老代码中放置ticket和Authorization时 会进行版本比较   大于3.4.0版本的才会放   当前版本肯定大于3.4.0
        if (data.toString().contains("ticket")) {
            data.remove("ticket");
        }
        //将请求Token放入params参数中
        data.put("Authorization", askQuestionReq.getBase().getAccessToken());
        data.put("source", askQuestionReq.getReqContent().getSource().toLowerCase());
        data.put("msgId", Util.getGuid());
        data.put("userId", askQuestionReq.getBase().getUid());
        data.put("deviceType", askQuestionReq.getBase().getOSType().toLowerCase().contains("android") ? "3" : "4");
        data.put("deviceid", askQuestionReq.getBase().getDeviceId());

        //调用接口获取返回值
        AskQuestionDto result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_ASK_QUESTION, data, askQuestionReq.getBase(), new TypeReference<AskQuestionDto>() {
        });

//        if (!ObjectUtil.equal(result.getStatus(), 1)) {
//            throw new AppException(result.getReason());
//        }

        return result;
    }

}
