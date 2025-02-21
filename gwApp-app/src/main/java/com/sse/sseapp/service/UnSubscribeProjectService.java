package com.sse.sseapp.service;

import cn.hutool.core.util.StrUtil;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.domain.system.*;
import com.sse.sseapp.feign.system.*;
import com.sse.sseapp.form.request.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : liuxinyu
 * @date : 2023/5/27 15:04
 */
@Service
@Slf4j
public class UnSubscribeProjectService {
    @Autowired
    ISysProjectMergeSubscriptionFeign mergeSubscriptionFeign;

    @Autowired
    ISysProjectKCBTBSubscriptionFeign kcbtbSubscriptionFeign;

    @Autowired
    ISysProjectKCBZRZSubscriptionFeign kcbzrzSubscriptionFeign;

    @Autowired
    ISysProjectSubscriptionFeign projectSubscriptionFeign;

    @Autowired
    ISysProjectDrSubscriptionFeign drSubscriptionFeign;

    @Autowired
    private CommonService commonService;

    public void unMergeSubscribeProject(BaseRequest<UnMergeSubscribeProjectReqBody> reqBody) {
        commonService.cominfoCheck(reqBody.getBase());
        try {
            String permitId = reqBody.getReqContent().getPassId();
            String phone = reqBody.getReqContent().getPhone();
            String projectId = reqBody.getReqContent().getProjectId();

            if (StrUtil.isEmpty(permitId) || StrUtil.isEmpty(phone) || StrUtil.isEmpty(projectId)) {
                throw new AppException("参数未输入");
            }
            log.info(phone + "取消订阅 projectId :" + projectId);
            SysProjectMergeSubscription subscribe = new SysProjectMergeSubscription();
            subscribe.setPassId(permitId);
            subscribe.setProjectId(projectId);
            subscribe.setUserName(phone);
            mergeSubscriptionFeign.unSubscribeProject(subscribe);


        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public void unRegisterSubscribeProject(BaseRequest<UnRegisterSubscribeProjectReqBody> reqBody) {
        commonService.cominfoCheck(reqBody.getBase());
        try {
            String permitId = reqBody.getReqContent().getPassId();
            String phone = reqBody.getReqContent().getPhone();
            String projectId = reqBody.getReqContent().getProjectId();

            if (StrUtil.isEmpty(permitId) || StrUtil.isEmpty(phone) || StrUtil.isEmpty(projectId)) {
                throw new AppException("参数未输入");
            }
            log.info(phone + "取消订阅 projectId :" + projectId);
            SysProjectSubscription subscribe = new SysProjectSubscription();
            subscribe.setPassId(permitId);
            subscribe.setProjectId(projectId);
            subscribe.setUserName(phone);
            projectSubscriptionFeign.unSubscribeProject(subscribe);
        } catch (Exception e) {
            throw new AppException(e);
        }

    }

    public void unRefinancingSubscribeProject(BaseRequest<UnRefinancingSubscribeProjectReqBody> reqBody) {
        commonService.cominfoCheck(reqBody.getBase());
        try {
            String permitId = reqBody.getReqContent().getPassId();
            String phone = reqBody.getReqContent().getPhone();
            String projectId = reqBody.getReqContent().getProjectId();

            if (StrUtil.isEmpty(permitId) || StrUtil.isEmpty(phone) || StrUtil.isEmpty(projectId)) {
                throw new AppException("参数未输入");
            }
            log.info(phone + "取消订阅 projectId :" + projectId);
            SysProjectKCBZRZSubscription subscribe = new SysProjectKCBZRZSubscription();
            subscribe.setPassId(permitId);
            subscribe.setProjectId(projectId);
            subscribe.setUserName(phone);

            kcbzrzSubscriptionFeign.unSubscribeProject(subscribe);


        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public void unSwitchBoardSubscribeProject(BaseRequest<UnSwitchBoardSubscribeProjectReqBody> reqBody) {
        commonService.cominfoCheck(reqBody.getBase());
        try {
            String permitId = reqBody.getReqContent().getPassId();
            String phone = reqBody.getReqContent().getPhone();
            String projectId = reqBody.getReqContent().getProjectId();

            if (StrUtil.isEmpty(permitId) || StrUtil.isEmpty(phone) || StrUtil.isEmpty(projectId)) {
                throw new AppException("参数未输入");
            }
            log.info(phone + "取消订阅 projectId :" + projectId);
            SysProjectKCBTBSubscription subscribe = new SysProjectKCBTBSubscription();
            subscribe.setPassId(permitId);
            subscribe.setProjectId(projectId);
            subscribe.setUserName(phone);

            kcbtbSubscriptionFeign.unSubscribeProject(subscribe);


        } catch (Exception e) {
            throw new AppException(e);
        }
    }
    public void unDrSubscribeProject(BaseRequest<UnDrSubscribeProjectReqBody> reqBody) {
        commonService.cominfoCheck(reqBody.getBase());
        try {
            String permitId = reqBody.getReqContent().getPassId();
            String phone = reqBody.getReqContent().getPhone();
            String projectId = reqBody.getReqContent().getProjectId();

            if (StrUtil.isEmpty(permitId) || StrUtil.isEmpty(phone) || StrUtil.isEmpty(projectId)) {
                throw new AppException("参数未输入");
            }
            log.info(phone + "取消订阅 projectId :" + projectId);
            SysProjectDrSubscription subscribe = new SysProjectDrSubscription();
            subscribe.setPassId(permitId);
            subscribe.setProjectId(projectId);
            subscribe.setUserName(phone);
            drSubscriptionFeign.unSubscribeProject(subscribe);
        } catch (Exception e) {
            throw new AppException(e);
        }
    }
}
