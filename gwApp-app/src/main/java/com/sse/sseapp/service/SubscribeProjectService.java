package com.sse.sseapp.service;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.domain.system.*;
import com.sse.sseapp.feign.system.*;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : liuxinyu
 * @date : 2023/5/27 11:47
 */
@Service
@Slf4j
public class SubscribeProjectService {

    @Autowired
    ISysDictTypeFeign iSysDictTypeFeign;

    @Autowired
    ISysProjectMergeSubscriptionFeign mergeSubscriptionFeign;

    @Autowired
    ISysProjectKCBTBSubscriptionFeign kcbtbSubscriptionFeign;

    @Autowired
    ISysProjectKCBZRZSubscriptionFeign kcbzrzSubscriptionFeign;

    @Autowired
    ISysProjectDrSubscriptionFeign drSubscriptionFeign;
    @Autowired
    ISysProjectSubscriptionFeign projectSubscriptionFeign;

    @Autowired
    private ProxyProvider proxyProvider;

    @Autowired
    private CommonService commonService;

    public void mergeSubscribeProject(BaseRequest<MergeSubscribeProjectReqBody> reqBody) {
        commonService.cominfoCheck(reqBody.getBase());
        try {
            String permitId = reqBody.getReqContent().getPassId();
            String phone = reqBody.getReqContent().getPhone();
            String projectId = reqBody.getReqContent().getProjectId();
            String projectName = reqBody.getReqContent().getProjectName();

            if (StrUtil.isEmpty(permitId) || StrUtil.isEmpty(phone) ||
                    StrUtil.isEmpty(projectId) || StrUtil.isEmpty(projectName)) {
                throw new AppException("输出参数为空");
            }
            String intermediaryId = reqBody.getReqContent().getIntermediaryId();
            if (StrUtil.isEmpty(intermediaryId)) {
                StringBuilder sb = new StringBuilder();

                Map<String, Object> param = new HashMap<>();
                param.put("stockAuditNum", projectId);
                // 中间机构类型，1=保荐机构 2=会计事务所 3=律师事务所 4=资产评估机构 5=其它
                param.put("intermediaryType", "1");
                param.put("token", "APPMQUERY");
                param.put("pageNo", "1");
                param.put("fields", "stockAuditNum,stockAuditName,auditApplyDate," +
                        "bussinesType,businessSubType,issueMarketType," +
                        "currStatus,registeResult,planIssueCapital,updateDate," +
                        "stockIssuer,intermediary,s_stockIssueId,s_issueCompanyFullName," +
                        "s_issueCompanyAbbrName,s_companyCode,s_companySecNameFull,i_intermediaryId," +
                        "i_intermediaryName,i_intermediaryAbbrName,i_intermediaryType,i_p_personId," +
                        "i_p_personName,i_p_jobType,i_p_jobTitle,i_person,i_p_orderNo,i_intermediaryOrder");
                // 调用 获取服务器返回结果
                SoaResponse<Map<String, Object>> proMSG22 = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_MERGE_PROJECT_LIST, param, reqBody.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
                });

                List<Map<String, Object>> proMSGList22 = proMSG22.getList();
                for (Map<String, Object> map : proMSGList22) {
                    List<Map<String, Object>> intermediarys = (List<Map<String, Object>>) map.get("intermediary");
                    for (Map<String, Object> intermediary : intermediarys) {
                        sb.append(intermediary.get("i_intermediaryId")).append(",");
                    }
                }
                String sbStr = sb.toString();
                if (sbStr.contains(",")) {
                    intermediaryId = sbStr.substring(0, sbStr.lastIndexOf(","));
                } else {
                    log.info("projectId:" + projectId + "没有对应的机构；intermediaryIds:" + intermediaryId);
                }
            }
            log.info(phone + "订阅了：projectId-》" + projectId + "  projectName->" + projectName);
            SysProjectMergeSubscription subscribe = new SysProjectMergeSubscription();
            subscribe.setPassId(permitId);
            subscribe.setProjectId(projectId);
            subscribe.setUserName(phone);
            subscribe.setProjectName(projectName);
            subscribe.setSubscriptionTime(new Date());
            subscribe.setIntermediaryId(intermediaryId);
            mergeSubscriptionFeign.subscribeProject(subscribe);
        } catch (AppException e) {
            throw new AppException(e);
        }
    }

    public void registerSubscribeProject(BaseRequest<RegisterSubscribeProjectReqBody> reqBody) {
        commonService.cominfoCheck(reqBody.getBase());
        try {
            String permitId = reqBody.getReqContent().getPassId();
            String phone = reqBody.getReqContent().getPhone();
            String projectId = reqBody.getReqContent().getProjectId();
            String projectName = reqBody.getReqContent().getProjectName();

            if (StrUtil.isEmpty(permitId) || StrUtil.isEmpty(phone) ||
                    StrUtil.isEmpty(projectId) || StrUtil.isEmpty(projectName)) {
                throw new AppException("输出参数为空");
            }
            String intermediaryId = reqBody.getReqContent().getIntermediaryId();
            if (StrUtil.isEmpty(intermediaryId)) {
                StringBuilder sb = new StringBuilder();

                Map<String, Object> param = new HashMap<>();
                param.put("stockAuditNum", projectId);
                param.put("intermediaryType", "1");
                param.put("token", "APPMQUERY");
                param.put("pageNo", "1");
                param.put("fields", "stockAuditNum,stockAuditName,auditApplyDate," +
                        "collectType,issueAmount,issueMarketType,wenHao,currStatus," +
                        "commitiResult,registeResult,suspendStatus,planIssueCapital," +
                        "createTime,updateDate,auditQianYiDate,stockIssuer,intermediary," +
                        "s_stockIssueId,s_issueCompanyFullName,s_issueCompanyAbbrName,s_csrcCode," +
                        "s_csrcCodeDesc,s_province,s_areaNameDesc,s_companyCode,s_personId,s_personName," +
                        "s_jobTitle,i_intermediaryId,i_intermediaryName,i_intermediaryAbbrName," +
                        "i_intermediaryType,i_p_personId,i_p_personName,i_p_jobType,i_p_jobTitle," +
                        "i_person,i_intermediaryOrder");
                // 调用 获取服务器返回结果
                SoaResponse<Map<String, Object>> proMSG22 = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_REGISTER_PROJECT_LIST, param, reqBody.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
                });
                List<Map<String, Object>> proMSGList22 = proMSG22.getList();
                for (Map<String, Object> map : proMSGList22) {
                    List<Map<String, Object>> intermediarys = (List<Map<String, Object>>) map.get("intermediary");
                    for (Map<String, Object> intermediary : intermediarys) {
                        sb.append(intermediary.get("i_intermediaryId")).append(",");
                    }
                }
                String sbStr = sb.toString();
                if (sbStr.contains(",")) {
                    intermediaryId = sbStr.substring(0, sbStr.lastIndexOf(","));
                } else {
                    log.info("projectId:" + projectId + "没有对应的机构；intermediaryIds:" + intermediaryId);
                }
            }

            log.info(phone + "订阅了：projectId-》" + projectId + "  projectName->" + projectName);
            SysProjectSubscription subscribe = new SysProjectSubscription();
            subscribe.setPassId(permitId);
            subscribe.setProjectId(projectId);
            subscribe.setUserName(phone);
            subscribe.setProjectName(projectName);
            subscribe.setSubscriptionTime(new Date());
            subscribe.setIntermediaryId(intermediaryId);
            projectSubscriptionFeign.subscribeProject(subscribe);

        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public void refinancingSubscribeProject(BaseRequest<RefinancingSubscribeProjectReqBody> reqBody) {
        commonService.cominfoCheck(reqBody.getBase());
        try {
            String permitId = reqBody.getReqContent().getPassId();
            String phone = reqBody.getReqContent().getPhone();
            String projectId = reqBody.getReqContent().getProjectId();
            String projectName = reqBody.getReqContent().getProjectName();
            if (StrUtil.isEmpty(permitId) || StrUtil.isEmpty(phone) ||
                    StrUtil.isEmpty(projectId) || StrUtil.isEmpty(projectName)) {
                throw new AppException("输出参数为空");
            }
            String intermediaryId = reqBody.getReqContent().getIntermediaryId();
            if (StrUtil.isEmpty(intermediaryId)) {
                StringBuilder sb = new StringBuilder();
                Map<String, Object> param = new HashMap<>();
                param.put("stockAuditNum", projectId);
                // 中间机构类型，1=保荐机构 2=会计事务所 3=律师事务所 4=资产评估机构 5=其它
                param.put("intermediaryType", "1");
                param.put("token", "APPMQUERY");
                param.put("pageNo", "1");
                param.put("fields", "stockAuditNum,stockAuditName,auditApplyDate," +
                        "bussinesType,issueMarketType,currStatus,registeResult," +
                        "suspendStatus,planIssueCapital,updateDate,stockIssuer," +
                        "intermediary,s_stockIssueId,s_issueCompanyFullName," +
                        "s_issueCompanyAbbrName,s_companyCode,s_companySecNameFull," +
                        "i_intermediaryId,i_intermediaryName,i_intermediaryAbbrName," +
                        "i_intermediaryType,i_p_personId,i_p_personName,i_p_jobType," +
                        "i_p_jobTitle,i_person,i_p_orderNo,i_intermediaryOrder");
                // 调用 获取服务器返回结果
                SoaResponse<Map<String, Object>> proMSG22 = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_KCBZRZ_PROJECT_LIST, param, reqBody.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
                });
                List<Map<String, Object>> proMSGList22 = proMSG22.getList();
                for (Map<String, Object> map : proMSGList22) {
                    List<Map<String, Object>> intermediarys = (List<Map<String, Object>>) map.get("intermediary");
                    for (Map<String, Object> intermediary : intermediarys) {
                        sb.append(intermediary.get("i_intermediaryId")).append(",");
                    }
                }
                String sbStr = sb.toString();
                if (sbStr.contains(",")) {
                    intermediaryId = sbStr.substring(0, sbStr.lastIndexOf(","));
                } else {
                    log.info("projectId:" + projectId + "没有对应的机构；intermediaryIds:" + intermediaryId);
                }
            }
            log.info(phone + "订阅了：projectId-》" + projectId + "  projectName->" + projectName);
            SysProjectKCBZRZSubscription subscribe = new SysProjectKCBZRZSubscription();
            subscribe.setPassId(permitId);
            subscribe.setProjectId(projectId);
            subscribe.setUserName(phone);
            subscribe.setProjectName(projectName);
            subscribe.setSubscriptionTime(new Date());
            subscribe.setIntermediaryId(intermediaryId);

            kcbzrzSubscriptionFeign.subscribeProject(subscribe);


        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public void switchBoardSubscribeProject(BaseRequest<SwitchBoardSubscribeProjectReqBody> reqBody) {
        commonService.cominfoCheck(reqBody.getBase());
        try {
            String permitId = reqBody.getReqContent().getPassId();
            String phone = reqBody.getReqContent().getPhone();
            String projectId = reqBody.getReqContent().getProjectId();
            String projectName = reqBody.getReqContent().getProjectName();
            if (StrUtil.isEmpty(permitId) || StrUtil.isEmpty(phone) ||
                    StrUtil.isEmpty(projectId) || StrUtil.isEmpty(projectName)) {
                throw new AppException("输出参数为空");
            }
            String intermediaryId = reqBody.getReqContent().getIntermediaryId();
            if (StrUtil.isEmpty(intermediaryId)) {
                StringBuilder sb = new StringBuilder();

                Map<String, Object> param = new HashMap<>();
                param.put("stockAuditNum", projectId);
                // 中间机构类型，1=保荐机构 2=律师事务所 3=会计师事务所4=评估机构5-其他
                param.put("intermediaryType", "1");
                param.put("token", "APPMQUERY");
                param.put("pageNo", "1");
                param.put("fields", "stockAuditNum,stockAuditName,auditApplyDate," +
                        "bussinesType,issueMarketType,currStatus,auditResult," +
                        "suspendStatus,listingBoardDate,updateDate,stockIssuer," +
                        "intermediary,s_stockIssueId,s_issueCompanyFullName," +
                        "s_issueCompanyAbbrName,s_companyCode,s_companySecNameFull," +
                        "i_intermediaryId,i_intermediaryName,i_intermediaryAbbrName," +
                        "i_intermediaryType,i_p_personId,i_p_personName,i_p_jobType," +
                        "i_p_jobTitle,i_person,i_p_orderNo,i_intermediaryOrder");

                // 调用 获取服务器返回结果
                SoaResponse<Map<String, Object>> proMSG22 = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_KCBTB_PROJECT_LIST, param, reqBody.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
                });
                List<Map<String, Object>> proMSGList22 = proMSG22.getList();
                for (Map<String, Object> map : proMSGList22) {
                    List<Map<String, Object>> intermediarys = (List<Map<String, Object>>) map.get("intermediary");
                    for (Map<String, Object> intermediary : intermediarys) {
                        sb.append(intermediary.get("i_intermediaryId")).append(",");
                    }
                }
                String sbStr = sb.toString();
                if (sbStr.contains(",")) {
                    intermediaryId = sbStr.substring(0, sbStr.lastIndexOf(","));
                } else {
                    log.info("projectId:" + projectId + "没有对应的机构；intermediaryIds:" + intermediaryId);
                }
            }

            log.info(phone + "订阅了：projectId-》" + projectId + "  projectName->" + projectName);
            SysProjectKCBTBSubscription subscribe = new SysProjectKCBTBSubscription();
            subscribe.setPassId(permitId);
            subscribe.setProjectId(projectId);
            subscribe.setUserName(phone);
            subscribe.setProjectName(projectName);
            subscribe.setSubscriptionTime(new Date());
            subscribe.setIntermediaryId(intermediaryId);

            kcbtbSubscriptionFeign.subscribeProject(subscribe);


        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public void drSubscribeProject(BaseRequest<DrSubscribeProjectReqBody> reqBody) {
        commonService.cominfoCheck(reqBody.getBase());
        try {
            String permitId = reqBody.getReqContent().getPassId();
            String phone = reqBody.getReqContent().getPhone();
            String projectId = reqBody.getReqContent().getProjectId();
            String projectName = reqBody.getReqContent().getProjectName();
            if (StrUtil.isEmpty(permitId) || StrUtil.isEmpty(phone) ||
                    StrUtil.isEmpty(projectId) || StrUtil.isEmpty(projectName)) {
                throw new AppException("输出参数为空");
            }
            String intermediaryId = reqBody.getReqContent().getIntermediaryId();
            if (StrUtil.isEmpty(intermediaryId)) {
                StringBuilder sb = new StringBuilder();

                Map<String, Object> param = new HashMap<>();
                param.put("stockAuditNum", projectId);
                // 中间机构类型，1=保荐机构 2=律师事务所 3=会计师事务所4=评估机构5-其他
                param.put("intermediaryType", "1");
                param.put("token", "APPMQUERY");
                param.put("pageNo", "1");
                param.put("fields", "stockAuditNum,stockAuditName,auditApplyDate," +
                        "bussinesType,issueMarketType,currStatus,auditResult," +
                        "suspendStatus,listingBoardDate,updateDate,stockIssuer," +
                        "intermediary,s_stockIssueId,s_issueCompanyFullName," +
                        "s_issueCompanyAbbrName,s_companyCode,s_companySecNameFull," +
                        "i_intermediaryId,i_intermediaryName,i_intermediaryAbbrName," +
                        "i_intermediaryType,i_p_personId,i_p_personName,i_p_jobType," +
                        "i_p_jobTitle,i_person,i_p_orderNo,i_intermediaryOrder");

                // 调用 获取服务器返回结果
                SoaResponse<Map<String, Object>> proMSG22 = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_DR_PROJECT_LIST, param, reqBody.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
                });
                List<Map<String, Object>> proMSGList22 = proMSG22.getList();
                for (Map<String, Object> map : proMSGList22) {
                    List<Map<String, Object>> intermediarys = (List<Map<String, Object>>) map.get("intermediary");
                    for (Map<String, Object> intermediary : intermediarys) {
                        sb.append(intermediary.get("i_intermediaryId")).append(",");
                    }
                }
                String sbStr = sb.toString();
                if (sbStr.contains(",")) {
                    intermediaryId = sbStr.substring(0, sbStr.lastIndexOf(","));
                } else {
                    log.info("projectId:" + projectId + "没有对应的机构；intermediaryIds:" + intermediaryId);
                }
            }

            log.info(phone + "订阅了：projectId-》" + projectId + "  projectName->" + projectName);
            SysProjectDrSubscription subscribe = new SysProjectDrSubscription();
            subscribe.setPassId(permitId);
            subscribe.setProjectId(projectId);
            subscribe.setUserName(phone);
            subscribe.setProjectName(projectName);
            subscribe.setSubscriptionTime(new Date());
            subscribe.setIntermediaryId(intermediaryId);

            drSubscriptionFeign.subscribeProject(subscribe);


        } catch (Exception e) {
            throw new AppException(e);
        }
    }
}