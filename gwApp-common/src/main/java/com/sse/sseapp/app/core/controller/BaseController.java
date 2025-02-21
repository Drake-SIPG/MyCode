package com.sse.sseapp.app.core.controller;

import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.domain.RespContentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 返回成功
     */
    public RespBean success() {
        logger.info("调用接口成功，返回成功信息");
        return RespBean.success();
    }

    /**
     * 返回成功消息
     */
    public RespBean success(String message) {
        logger.info("调用接口成功，返回成功信息，成功信息为：{}", message);
        return RespBean.success(message);
    }

    /**
     * 返回成功消息
     */
    public <T extends RespContentVO> RespBean<T> success(T data) {
        logger.info("调用接口成功，返回成功信息，成功数据为：{}", data);
        return RespBean.success(data);
    }

    /**
     * 返回失败消息
     */
    public RespBean error() {
        logger.error("调用接口失败，返回失败信息");
        return RespBean.error();
    }

    /**
     * 返回失败消息
     */
    public RespBean error(String message) {
        logger.error("调用接口失败，返回失败信息，失败信息为：{}", message);
        return RespBean.error(message);
    }

    /**
     * 返回无效信息消息
     */
    public RespBean decrypt(String message) {
        logger.warn("调用接口被拒绝，返回拒绝信息，拒绝愿意为：{}", message);
        return RespBean.decrypt(message);
    }

    //    public boolean checkUser(){
    //        String appVersion = Util.getObjStrV(getDeviceInfo().getAv());
    //        String uid = Util.getObjStrV(getDeviceInfo().getUID());
    //        if(uid.trim().equals("")) {
    //            getDeviceInfo().setAccessToken("");
    //            getDeviceInfo().setUID("");
    //            setBaseInfo();
    //            setFail(AppReturnCode.NotLoggedInError);
    //            renderJsonp(getResultMap());
    //            return false;
    //        }
    //
    //        String accessToken = Util.getObjStrV(getDeviceInfo().getAccessToken());
    //        if(accessToken.trim().equals("")) {
    //            getDeviceInfo().setAccessToken("");
    //            getDeviceInfo().setUID("");
    //            setBaseInfo();
    //            setFail(AppReturnCode.NotLoggedInError);
    //            renderJsonp(getResultMap());
    //            return false;
    //        }
    //
    //        //调用 通用框架拦截器
    //        String resultCommon = checkUserCommon();
    //        JSONObject resultJson = JSONObject.parseObject(resultCommon);
    //        String rstatus = resultJson.get("state") + "";
    //        String rmsg = resultJson.get("msg") + "";
    //        if(!"1".equals(rstatus)) {
    //            logger.error(" *** === commonserver 拦截器  : " + rstatus + " ...... " + rmsg);
    //            setBaseInfo();
    //            setFail(rstatus, rmsg);
    //            renderJsonp(getResultMap());
    //            return false;
    //        }
    //        return true;
    //    }
}
